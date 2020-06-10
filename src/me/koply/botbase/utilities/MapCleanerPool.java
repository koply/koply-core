package me.koply.botbase.utilities;

import me.koply.botbase.App;
import me.koply.botbase.data.ConfigManager;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Koply
 * @since 03/06/2020
 */
public final class MapCleanerPool {
    public MapCleanerPool(ConcurrentMap<String, Long> cooldownList) {
        this.cooldownList = cooldownList;
    }

    private final ConcurrentMap<String, Long> cooldownList;
    private int cooldown = 0;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public final void asyncCleaner() {
        final Runnable task = this::cleaner;
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(task, 1L, 1L, TimeUnit.MINUTES);
        cooldown = ConfigManager.getInstance().getCooldown();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduledFuture.cancel(true);
            scheduledExecutorService.shutdownNow();
        }, "Shutdown-thread"));
    }

    private void cleaner() {
        long currentMillis = System.currentTimeMillis();
        int i = 0;
        for (Map.Entry<String, Long> entry : cooldownList.entrySet()) {
            if (currentMillis - entry.getValue() >= cooldown) {
                cooldownList.remove(entry.getKey());
                i++;
            }
        }
        App.logger.info("Removed " + i + " entries deleted from cooldown list.");
    }
}