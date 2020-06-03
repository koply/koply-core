package me.koply.botbase.commandsystem;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * @author Koply
 * @since 03/06/2020
 */
public class CommandParameters {

    public CommandParameters(MessageReceivedEvent e, Message msg, TextChannel textChan, Member mem, String[] args) {
        event = e;
        message = msg;
        textChannel = textChan;
        member = mem;
        this.args = args;
    }

    private MessageReceivedEvent event;
    private Message message;
    private TextChannel textChannel;
    private Member member;
    private String[] args;

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public CommandParameters setEvent(MessageReceivedEvent event) {
        this.event = event;
        return this;
    }

    public Message getMessage() {
        return message;
    }

    public CommandParameters setMessage(Message message) {
        this.message = message;
        return this;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public CommandParameters setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
        return this;
    }

    public Member getMember() {
        return member;
    }

    public CommandParameters setMember(Member member) {
        this.member = member;
        return this;
    }

    public String[] getArgs() {
        return args;
    }
}