package controller.cli.cmd;

import controller.Controller;
import model.data.PillSet;

import java.util.List;
import java.util.Objects;

public class ScheduleCmd extends Cmd {
    public ScheduleCmd(String name, Controller controller) {
        super(name, controller);
    }

    // schedule once <schedule id> <pillset id> <delay_sec / time>
    // schedule repeat <schedule id> <pillset id> <delay_sec / time> <interval_sec / time>
    // schedule edit once <schedule id> <pillset id> <time>
    // schedule edit repeat <schedule id> <pillset id> <delay_sec / time> <interval_sec / time>
    // schedule remove <schedule id>
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        boolean err = false;
        if (args.isEmpty()) {
            controller.model.alarm.printTasks();
        }
        else if (Objects.equals(args.get(0), "once")) {
            if (args.size() == 4) {
                try {
                    String id = args.get(1);
                    String pillSetId = args.get(2);
                    long delay = Long.parseLong(args.get(3));
                    PillSet pillSet = controller.model.pillSets.getPillSet(pillSetId);
                    if (pillSet == null) {
                        System.out.println("ERROR : No such pillset. id : " + pillSetId);
                        return;
                    }
                    // !note!ここあとで
                    controller.model.alarm.scheduleOneTask(id, controller.model.alarm.toTask(pillSet), delay);
                } catch (NumberFormatException e) {
                    err = true;
                }
            } else {
                err = true;
            }
        }
        else if (Objects.equals(args.get(0), "repeat") || Objects.equals(args.get(0), "rep")) {
            if (args.size() == 5) {
                try {
                    String id = args.get(1);
                    String pillSetId = args.get(2);
                    long delay = Long.parseLong(args.get(3));
                    long interval = Long.parseLong(args.get(4));
                    PillSet pillSet = controller.model.pillSets.getPillSet(pillSetId);
                    if (pillSet == null) {
                        System.out.println("ERROR : No such pillset. id : " + pillSetId);
                        return;
                    }
                    controller.model.alarm.schedulePeriodicTask(id, controller.model.alarm.toTask(pillSet), delay, interval);
                } catch (NumberFormatException e) {
                    err = true;
                }
            } else {
                err = true;
            }

        }
        else if (Objects.equals(args.get(0), "edit") && Objects.equals(args.get(1), "once")) {
            if (args.size() == 5) {
                try {
                    String id = args.get(2);
                    String pillSetId = args.get(3);
                    long delay = Long.parseLong(args.get(4));
                    PillSet pillSet = controller.model.pillSets.getPillSet(pillSetId);
                    if (pillSet == null) {
                        System.out.println("ERROR : No such pillset. id : " + pillSetId);
                        return;
                    }
                    controller.model.alarm.rescheduleOneTask(id, controller.model.alarm.toTask(pillSet), delay);
                } catch (NumberFormatException e) {
                    err = true;
                }
            } else {
                err = true;
            }
        }
        else if (Objects.equals(args.get(0), "edit") && Objects.equals(args.get(1), "repeat")) {
            if (args.size() == 6) {
                try {
                    String id = args.get(2);
                    String pillSetId = args.get(3);
                    long delay = Long.parseLong(args.get(4));
                    long interval = Long.parseLong(args.get(5));
                    PillSet pillSet = controller.model.pillSets.getPillSet(pillSetId);
                    if (pillSet == null) {
                        System.out.println("ERROR : No such pillset. id : " + pillSetId);
                        return;
                    }
                    controller.model.alarm.reschedulePeriodicTask(id, controller.model.alarm.toTask(pillSet), delay, interval);
                } catch (NumberFormatException e) {
                    err = true;
                }
            } else {
                err = true;
            }
        }
        else if (Objects.equals(args.get(0), "remove")) {
            if (args.size() == 2) {
                controller.model.alarm.cancelTask(args.get(1));
            } else {
                err = true;
            }
        }
        else {
            err = true;
        }
        if (err) {
            System.out.println("ERROR : Invalid arguments.");
            System.out.println("Usage : schedule once <schedule id> <pillset id> <delay_sec>");
            System.out.println("Usage : schedule repeat <schedule id> <pillset id> <delay_sec> <interval_sec>");
            System.out.println("Usage : schedule edit once <schedule id> <pillset id> <delay_sec>");
            System.out.println("Usage : schedule edit repeat <schedule id> <pillset id> <delay_sec> <interval_sec>");
            System.out.println("Usage : schedule remove <schedule id>");
        }
    }
}
