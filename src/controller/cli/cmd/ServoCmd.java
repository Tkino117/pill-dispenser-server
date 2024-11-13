package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class ServoCmd extends Cmd {
    public ServoCmd(String name, Controller controller) {
        super(name, controller);
    }

    // test
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        if (args.size() == 2) {
            int id, deg;
            try {
                id = Integer.parseInt(args.get(0));
                deg = Integer.parseInt(args.get(1));
            }
            catch (NumberFormatException e) {
                System.out.println("ERROR : Invalid arguments.");
                System.out.println("Usage : servo <id> <degree>");
                return;
            }
            controller.model.server.adjustServo(id, deg);
        }
        else {
            System.out.println("ERROR : Invalid arguments.");
            System.out.println("Usage : servo <id> <degree>");
        }
    }
}
