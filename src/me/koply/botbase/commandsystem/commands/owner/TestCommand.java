package me.koply.botbase.commandsystem.commands.owner;

import me.koply.botbase.commandsystem.Command;
import me.koply.botbase.commandsystem.CommandParameters;
import me.koply.botbase.commandsystem.annotations.*;
import me.koply.botbase.data.DataManager;
import me.koply.botbase.data.GuildData;

/**
 * @author Koply
 * @since 12/06/2020
 */
@OwnerOnly
@CommandName("test")
@CommandDescription("Test command for bot owners.")
public final class TestCommand extends Command {

    @Override
    public final void handle(CommandParameters cmd) {
        GuildData guildData = DataManager.getInstance().getGuildData(cmd.getEvent().getGuild().getId());

        String desc = guildData.getDescription();
        if (desc == null) {
            cmd.getTextChannel().sendMessage("Last desc null").queue();
        } else {
            cmd.getTextChannel().sendMessage("Last desc: " + desc).queue();
        }

        guildData.setDescription(cmd.getMessage().getContentRaw().substring(5));

        DataManager.getInstance().saveDatas();
    }
}