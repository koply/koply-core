# Koply Core
Simple bot base includes CommandHandler and Config system.

# How To Create A Command
```java
@CommandName("ping")
@CommandDescription("Pong!")
public final class PingCommand extends Command {

    @Override
    public final void handle(CommandParameters cmd) {
        cmd.getTextChannel().sendMessage("Pong!").queue();
    }
}
```

`@CommandName`: Command's name for usage. 

`@CommandDescription`: Command's description for help command.

`@OwnerOnly`: This command usable for only bot owners.

# Config

`"prefix": "$"`: For command prefix.

`"token": "INSERT-BOT-TOKEN-HERE"`: Bot token.

`"owners": ["INSERT-YOUR-ID-HERE"],`: Bot owner id's.

`"cooldown": 5000`: Cooldown for regular users. The bot owners are doesn't affect the cooldown.

# Group Names And Descriptions

`"info": "Bilgilendirme--Bilgi alabileceğiniz komutlar bulunur."`: The key named as 'info' for 'info' commands package. The group name is before then '--'. The group description is after then '--'.

`"nulldescriptiontext": "Açıklama girilmedi."`: This line for non-descriptioned commands.




