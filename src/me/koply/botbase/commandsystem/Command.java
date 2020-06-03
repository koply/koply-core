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

    public void setOnlyGuild() {
        onlyGuild = true;
    }

    public boolean isOnlyGuild() {
        return onlyGuild;
    }

    public void setOnlyOwner() { onlyOwner = true; }

    public boolean isOnlyOwner() { return onlyOwner; }

    public void setGroup(String group1) { group = group1; }

    public String getGroup() { return group; }

    public void setDescription(String desc) {
        description = desc;
    }

    public String getDescription() {
        return description;
    }
}