package model.alarm;

import controller.Controller;
import model.data.PillSet;
import model.server.ServerManager;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class AlarmManager {
    private final ScheduledExecutorService scheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> tasks;
    private final ServerManager server;
    private final Controller controller;

    public AlarmManager(ServerManager server, Controller controller) {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.tasks = new ConcurrentHashMap<>();
        this.server = server;
        this.controller = controller;
    }

    // one time task
    public void scheduleOneTask(String id, Runnable task, long delay_sec) {
        if (tasks.containsKey(id)) {
            System.out.println("ERROR : Already scheduled task. id : " + id);
            return;
        }
        Runnable taskWrapper = () -> {
            task.run();
            tasks.remove(id);
        };
        ScheduledFuture<?> future = scheduler.schedule(taskWrapper, delay_sec, java.util.concurrent.TimeUnit.SECONDS);
        tasks.put(id, future);
    }
    public void scheduleOneTask(String id, Runnable task, LocalDateTime executeTime) {
        long delay = java.time.Duration.between(LocalDateTime.now(), executeTime).getSeconds();
        scheduleOneTask(id, task, delay);
    }
    public void rescheduleOneTask(String id, Runnable task, long delay_sec) {
        if (cancelTask(id)) {
            scheduleOneTask(id, task, delay_sec);
            return;
        }
    }
    public void rescheduleOneTask(String id, Runnable task, LocalDateTime executeTime) {
        if (cancelTask(id)) {
            scheduleOneTask(id, task, executeTime);
            return;
        }
        System.out.println("ERROR : No such task. id : " + id);
    }

    // periodic task
    public void schedulePeriodicTask(String id, Runnable task, long delay_sec, long period_sec) {
        if (tasks.containsKey(id)) {
            System.out.println("ERROR : Already scheduled task. id : " + id);
            return;
        }
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(task, delay_sec, period_sec, java.util.concurrent.TimeUnit.SECONDS);
        tasks.put(id, future);
    }
    public void schedulePeriodicTask(String id, Runnable task, LocalDateTime executeTime, long period_sec) {
        long delay = java.time.Duration.between(LocalDateTime.now(), executeTime).getSeconds();
        schedulePeriodicTask(id, task, delay, period_sec);
    }
    public void reschedulePeriodicTask(String id, Runnable task, long delay_sec, long period_sec) {
        if (cancelTask(id)) {
            schedulePeriodicTask(id, task, delay_sec, period_sec);
            return;
        }
        System.out.println("ERROR : No such task. id : " + id);
    }
    public void reschedulePeriodicTask(String id, Runnable task, LocalDateTime executeTime, long period_sec) {
        if (cancelTask(id)) {
            schedulePeriodicTask(id, task, executeTime, period_sec);
            return;
        }
        System.out.println("ERROR : No such task. id : " + id);
    }

    public Runnable toTask(PillSet pillSet) {
        return () -> {
            server.dispensePillSet(pillSet, controller.view);
        };
    }
    public boolean cancelTask(String id, boolean displayLog) {
        ScheduledFuture<?> future = tasks.get(id);
        if (future != null) {
            future.cancel(true);
            tasks.remove(id);
            return true;
        }
        if (displayLog) System.out.println("ERROR : No such task. id : " + id);
        return false;
    }
    public boolean cancelTask(String id) {
        return cancelTask(id, true);
    }
    public void shutdown() {
        scheduler.shutdown();
    }
    public void printTasks() {
        System.out.println("Scheduled tasks : ");
        for (String id : tasks.keySet()) {
            System.out.println("id : " + id + " : " + tasks.get(id).getDelay(java.util.concurrent.TimeUnit.SECONDS) + " sec");
        }
    }
}
