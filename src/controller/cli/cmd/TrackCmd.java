package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class TrackCmd extends Cmd {
    public TrackCmd(String name, Controller controller) {
        super(name, controller);
    }

    // track
    // track clear
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        if (args.isEmpty()) {
            controller.model.pillTracker.printLast();
        } else if (args.size() == 1 && args.get(0).equals("clear")) {
            controller.model.pillTracker.clear();
        } else {
            System.out.println("Invalid arguments");
            System.out.println("Usage: track");
            System.out.println("Usage: track clear");
        }
    }
}
