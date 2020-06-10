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

    public CommandParameters(MessageReceivedEvent e, Message msg, TextChannel textChan, Member mem, String[] args) {
        event = e;
        message = msg;
        textChannel = textChan;
        member = mem;
        this.args = args;
    }

    private final MessageReceivedEvent event;
    private final Message message;
    private final TextChannel textChannel;
    private final Member member;
    private final String[] args;

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public Message getMessage() {
        return message;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public Member getMember() {
        return member;
    }

    public String[] getArgs() {
        return args;
    }
}