package me.koply.botbase.commandsystem.commands.tools;

import me.koply.botbase.commandsystem.Command;
import me.koply.botbase.commandsystem.CommandParameters;
import me.koply.botbase.commandsystem.annotations.CommandDescription;
import me.koply.botbase.commandsystem.annotations.CommandName;
import me.koply.botbase.commandsystem.annotations.GuildOnly;
import me.koply.botbase.data.DataManager;
import me.koply.botbase.utilities.Util;
import net.dv8tion.jda.api.Permission;

/**
 * @author Koply
 * @since 12/06/2020
 */
@GuildOnly
@CommandName("description")
@CommandDescription("Description command for current guild.")
public final class DescriptionCommand extends Command {

    @Override
    public final void handle(CommandParameters cmd) {

        if (!cmd.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            cmd.getTextChannel().sendMessage(Util.basicEmbed("Bu komutu kullanabilmek için yönetici olmalısınız.").build()).queue();
            return;
        }

        if (cmd.getArgs().length == 1) {
            String currentDesc = DataManager.getInstance().getGuildData(cmd.getEvent().getGuild().getId()).getDescription();
            if (currentDesc == null || currentDesc.equals("")) {
                cmd.getTextChannel().sendMessage(Util.basicEmbed("Şu anda açıklama bulunmuyor.").build()).queue();
            } else {
                cmd.getTextChannel().sendMessage(Util.basicEmbed("Sunucu açıklaması: `" + currentDesc + "`").build()).queue();
            }
        } else if (cmd.getArgs()[1].equalsIgnoreCase("set")) {
            String desc = cmd.getMessage().getContentRaw().substring(cmd.getArgs()[0].length() + cmd.getArgs()[1].length() + cmd.getPrefix().length() + 2);
            DataManager.getInstance().getGuildData(cmd.getEvent().getGuild().getId()).setDescription(desc);
            cmd.getTextChannel().sendMessage(Util.basicEmbed("Sunucu açıklaması `" + desc + "` olarak ayarlandı.").build()).queue();
        } else {
            cmd.getTextChannel().sendMessage(Util.basicEmbed(cmd.getPrefix() + "description -> Şu anki açıklamayı gösterir."
                                                     +"\n" + cmd.getPrefix() + "description set <açıklama> -> Sunucu açıklamasını girdiğiniz açıklama olarak ayarlar.").build()).queue();
        }
    }
}