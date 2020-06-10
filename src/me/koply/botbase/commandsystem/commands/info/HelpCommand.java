package me.koply.botbase.commandsystem.commands.info;

import me.koply.botbase.commandsystem.Command;
import me.koply.botbase.commandsystem.CommandHandler;
import me.koply.botbase.commandsystem.CommandParameters;
import me.koply.botbase.commandsystem.annotations.CommandDescription;
import me.koply.botbase.commandsystem.annotations.CommandName;
import net.dv8tion.jda.api.EmbedBuilder;

/**
 * @author Koply
 * @since 03/06/2020
 */
@CommandName("help")
@CommandDescription("Botta bulunan komutları görmenize yarar.")
public final class HelpCommand extends Command {

    @Override
    public final void handle(CommandParameters cmd) {
        if (cmd.getArgs().length == 1) {
            EmbedBuilder eb = CommandHandler.getInstance().getGroupEmbeds().get("main")
                    .setAuthor(cmd.getEvent().getJDA().getSelfUser().getName(), null, cmd.getEvent().getJDA().getSelfUser().getAvatarUrl());
            cmd.getTextChannel().sendMessage(eb.build()).queue();
        } else {
            if (!CommandHandler.getInstance().getGroupEmbeds().containsKey(cmd.getArgs()[1].toLowerCase())) {
                cmd.getMessage().addReaction("⛔").queue();
                return;
            }

            EmbedBuilder eb = CommandHandler.getInstance().getGroupEmbeds().get(cmd.getArgs()[1].toLowerCase())
                    .setAuthor(cmd.getEvent().getJDA().getSelfUser().getName(), null, cmd.getEvent().getJDA().getSelfUser().getAvatarUrl());
            cmd.getTextChannel().sendMessage(eb.build()).queue();
        }
    }
}