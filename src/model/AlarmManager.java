package model;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class AlarmManager {
    private final ScheduledExecutorService scheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> tasks;

    public AlarmManager() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.tasks = new ConcurrentHashMap<>();
    }

    // one time task
    public void scheduleOneTask(String id, Runnable task, long delay_sec) {
        ScheduledFuture<?> future = scheduler.schedule(task, delay_sec, java.util.concurrent.TimeUnit.SECONDS);
        tasks.put(id, future);
    }
    public void scheduleOneTask(String id, Runnable task, LocalDateTime executeTime) {
        long delay = java.time.Duration.between(LocalDateTime.now(), executeTime).getSeconds();
        scheduleOneTask(id, task, delay);
    }

    // periodic task
    public void schedulePeriodicTask(String id, Runnable task, long delay_sec, long period_sec) {
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(task, delay_sec, period_sec, java.util.concurrent.TimeUnit.SECONDS);
        tasks.put(id, future);
    }
    public void schedulePeriodicTask(String id, Runnable task, LocalDateTime executeTime, long period_sec) {
        long delay = java.time.Duration.between(LocalDateTime.now(), executeTime).getSeconds();
        schedulePeriodicTask(id, task, delay, period_sec);
    }

    public void cancelTask(String id) {
        ScheduledFuture<?> future = tasks.get(id);
        if (future != null) {
            future.cancel(true);
            tasks.remove(id);
        }
    }
    public void shutdown() {
        scheduler.shutdown();
    }
}
