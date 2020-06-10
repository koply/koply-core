package me.koply.botbase.commandsystem;

import me.koply.botbase.commandsystem.annotations.CommandDescription;
import me.koply.botbase.commandsystem.annotations.CommandName;
import me.koply.botbase.commandsystem.annotations.GuildOnly;
import me.koply.botbase.data.ConfigManager;
import me.koply.botbase.utilities.MapCleanerPool;
import me.koply.botbase.App;
import me.koply.botbase.utilities.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Koply, MegaCrafter
 * @since 03/06/2020
 */
public final class CommandHandler extends ListenerAdapter {

    // commands map and getter
    private final HashMap<String, Command> commands = new HashMap<>(); // command, object
    public final HashMap<String, Command> getCommands() { return commands; }

    private final ConcurrentMap<String, Long> cooldownList = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private String prefix;
    private List<String> ownerList;

    // help group messages
    private final Map<String, EmbedBuilder> groupEmbeds = new HashMap<>();
    public final Map<String, EmbedBuilder> getGroupEmbeds() { return groupEmbeds; }

    private static CommandHandler instance;
    public static CommandHandler getInstance() {
        if (instance == null) instance = new CommandHandler();
        return instance;
    }

    private boolean init = false;
    public final void init(String prefix, List<String> ownerList) {
        if (init) return;
        init = true;
        this.prefix = prefix;
        this.ownerList = ownerList;
        registerCommands();
        new MapCleanerPool(cooldownList).asyncCleaner();
        generateHelpEmbeds();
    }

    @Override
    public final void onMessageReceived(@Nonnull MessageReceivedEvent e) {
        if (e.isWebhookMessage() || e.getAuthor().isBot()) {
            return;
        }

        String commandRaw = e.getMessage().getContentRaw();
        // TODO: Prefix from database for this guild
        if (!commandRaw.startsWith(prefix)) {
            return;
        }

        String guildName;
        if (e.isFromGuild()) {
            guildName = e.getGuild().getName();
        } else {
            guildName = "(PRIVATE)";
        }

        String[] cmdArgs = commandRaw.substring(prefix.length()).split(" ");
        App.logger.info(String.format("Command received | User: %s | Guild: %s | Command: %s", e.getAuthor().getAsTag(), guildName, commandRaw));

        Command command = commands.get(cmdArgs[0]);
        if (command == null) {
            App.logger.info("Last command was not a valid command.");
            return;
        }

        String authorID = e.getAuthor().getId();
        if (cooldownCheck(authorID) && !ownerList.contains(authorID)) {
            App.logger.info("Last command has been declined due to cooldown check");
            return;
        }

        if (command.isOnlyGuild() && !e.isFromGuild()) {
            App.logger.info("GuildOnly command used from private channel");
            return;
        }

        if (command.isOnlyOwner() && !ownerList.contains(authorID)) {
            App.logger.info("OwnerOnly command used by non owner user.");
            return;
        }

        CommandParameters commandParameters = new CommandParameters(e, e.getMessage(), e.getTextChannel(), e.getMember(), cmdArgs);
        App.logger.info("Last command has been submitted to ExecutorService");
        long firstTime = System.currentTimeMillis();
        cooldownList.put(authorID, firstTime);
        try {
            executorService.submit(() -> {
                command.handle(commandParameters);
                App.logger.info("Last command took " + (System.currentTimeMillis() - firstTime) + "ms to execute.");
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private boolean cooldownCheck(String userID) {
        long listTime = cooldownList.getOrDefault(userID, 0L);
        if (listTime == 0) return false;
        else return System.currentTimeMillis() - listTime <= 5000;
    }

    private void generateHelpEmbeds() {
        Map<String, List<Command>> groupCommands = new HashMap<>(); // groupname, commands

        StringBuilder mainEmbed = new StringBuilder();
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            String[] splittedGroupText = entry.getValue().getGroup().split("--");
            mainEmbed.append("`").append(splittedGroupText[0]).append("` - ").append(splittedGroupText[1]).append("\n");
            groupCommands.computeIfAbsent(splittedGroupText[0], s -> new ArrayList<>());
            groupCommands.get(splittedGroupText[0]).add(entry.getValue());
        }

        EmbedBuilder mainBuilder = new EmbedBuilder()
                .setColor(Util.randomColor())
                .addField("❯ Kullanım", "`" + ConfigManager.getInstance().getPrefix() + "help <grup>` şeklinde kullanabilirsiniz. \nGruplar aşağıda listelenmiştir.", false)
                .addField("❯ Gruplar", mainEmbed.toString(), false)
                .addField("❯ Bazı Linkler", "[Koply-Core](https://github.com/MusaBrt/koply-core)", false);
        groupEmbeds.put("main", mainBuilder);


        for (Map.Entry<String, List<Command>> entry : groupCommands.entrySet()) {

            StringBuilder sb = new StringBuilder();
            for (Command cmd : entry.getValue()) {
                sb.append("`").append(cmd.getClass().getAnnotation(CommandName.class).value()[0]).append("` - ").append(cmd.getDescription()).append("\n");
            }

            EmbedBuilder commandBuilder = new EmbedBuilder()
                    .setColor(Util.randomColor())
                    .addField("❯ " + entry.getKey() + " Grubu Komutları", sb.toString(), false)
                    .addField("❯ Bazı Linkler", "[Koply-Core](https://github.com/MusaBrt/koply-core)", false);

            groupEmbeds.put(entry.getKey().toLowerCase(), commandBuilder);
        }
    }

    private void registerCommands() {
        Reflections reflections = new Reflections(CommandHandler.class.getPackage().getName() + ".commands");
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);

        Map<String, String> groupReplaceList = ConfigManager.getInstance().getGroupReplaceList();
        String nullDescriptionText = groupReplaceList.get("nulldescriptiontext");

        for (Class<? extends Command> claz : classes) {
            CommandName ci = claz.getAnnotation(CommandName.class);

            String[] groupNameSplit = claz.getPackage().getName().split("\\.");
            String groupRawName = groupNameSplit[groupNameSplit.length - 1]; // package name
            String groupReplacedName = groupReplaceList.get(groupRawName); // config.json display name

            try {
                Command command = claz.newInstance();

                GuildOnly guildOnly = claz.getAnnotation(GuildOnly.class);
                if (guildOnly != null) {
                    command.setOnlyGuild();
                }

                CommandDescription commandDescription = claz.getAnnotation(CommandDescription.class);
                String commandDescriptionText = nullDescriptionText;
                if (commandDescription != null) {
                    commandDescriptionText = commandDescription.value();
                }
                command.setDescription(commandDescriptionText);
                command.setGroup(groupReplacedName);

                for (String str : ci.value()) {
                    commands.put(str, command);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}