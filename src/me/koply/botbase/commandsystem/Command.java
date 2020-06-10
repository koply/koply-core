package me.koply.botbase.commandsystem;

/**
 * @author Koply
 * @since 03/06/2020
 */
public abstract class Command {
    public abstract void handle(CommandParameters cmd);
    private boolean onlyGuild;
    private boolean onlyOwner;
    private String description;
    private String group;

    public final void setOnlyGuild() {
        onlyGuild = true;
    }

    public final boolean isOnlyGuild() {
        return onlyGuild;
    }

    public final void setOnlyOwner() { onlyOwner = true; }

    public final boolean isOnlyOwner() { return onlyOwner; }

    public final void setGroup(String group1) { group = group1; }

    public final String getGroup() { return group; }

    public final void setDescription(String desc) {
        description = desc;
    }

    public final String getDescription() {
        return description;
    }
}