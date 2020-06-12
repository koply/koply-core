package me.koply.botbase.commandsystem;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * @author Koply
 * @since 03/06/2020
 */
public final class CommandParameters {

    public CommandParameters(MessageReceivedEvent e, Message msg, TextChannel textChan, Member mem, String[] args, String prefix) {
        event = e;
        message = msg;
        textChannel = textChan;
        member = mem;
        this.args = args;
        this.prefix = prefix;
    }

    private final MessageReceivedEvent event;
    private final Message message;
    private final TextChannel textChannel;
    private final Member member;
    private final String[] args;
    private final String prefix;

    public final MessageReceivedEvent getEvent() {
        return event;
    }

    public final Message getMessage() {
        return message;
    }

    public final TextChannel getTextChannel() {
        return textChannel;
    }

    public final Member getMember() {
        return member;
    }

    public final String[] getArgs() {
        return args;
    }

    public final String getPrefix() { return prefix; }
}