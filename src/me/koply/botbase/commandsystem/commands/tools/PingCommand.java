package me.koply.botbase.commandsystem.commands.tools;

import me.koply.botbase.commandsystem.Command;
import me.koply.botbase.commandsystem.CommandParameters;
import me.koply.botbase.commandsystem.annotations.CommandDescription;
import me.koply.botbase.commandsystem.annotations.CommandName;

/**
 * @author Koply
 * @since 03/06/2020
 */
@CommandName("ping")
@CommandDescription("Pong!")
public class PingCommand extends Command {

    @Override
    public void handle(CommandParameters cmd) {
        cmd.getTextChannel().sendMessage("Pong!").queue();
    }
}