package me.koply.botbase.utilities;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Koply
 * @since 03/06/2020
 */
public class MapCleanerPool {
    public MapCleanerPool(ConcurrentMap<String, Long> cooldownList) {
        this.cooldownList = cooldownList;
    }

    private final ConcurrentMap<String, Long> cooldownList;

    public void asyncCleaner() {
        new Thread(this::timedCleaner).start();
    }

    private void timedCleaner() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                cleaner();
            }
        };
        timer.schedule(timerTask, 60000, 60000);
    }

    private void cleaner() {
        long currentMillis = System.currentTimeMillis();
        for (Map.Entry<String, Long> entry : cooldownList.entrySet()) {
            if (currentMillis - entry.getValue() >= 5000) cooldownList.remove(entry.getKey());
        }
    }
}