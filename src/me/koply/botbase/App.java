package me.koply.botbase;

import me.koply.botbase.commandsystem.CommandHandler;
import me.koply.botbase.data.ConfigManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author Koply
 * @since 03/06/2020
 */
public final class App {

    public static final Logger logger = Logger.getLogger("App");

    public App() throws LoginException, InterruptedException {
        logger.info("Hello, World!");
        ConfigManager configManager = ConfigManager.getInstance().loadConfig();
        JDA jda = JDABuilder.createDefault(configManager.getToken()).setAutoReconnect(true).build();
        jda.awaitReady();
        CommandHandler.getInstance().init(configManager.getPrefix(), configManager.getOwnerList(), configManager.getCooldown());
        jda.addEventListener(CommandHandler.getInstance());

        setupLogger();
    }

    private void setupLogger() {
        logger.setUseParentHandlers(false);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Formatter() {
            private final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

            @Override
            public String format(LogRecord record) {
                // [22/04/2020 18:01:30.533 INFO] Message

                return String.format("[%s %s] %s\n", formatter.format(new Date(record.getMillis())), record.getLevel(), record.getMessage());
            }
        });
        logger.addHandler(consoleHandler);
    }

    public static void main(String[] args) {
        try {
            new App();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}