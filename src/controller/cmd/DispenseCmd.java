package controller.cmd;

import controller.Controller;

import java.util.List;

public class DispenseCmd extends Cmd {
    public DispenseCmd(String name, Controller controller) {
        super(name, controller);
    }

    @Override
    public void execute(List<String> args) {
        if (args.size() != 2) {
            System.out.println("ERROR : Invalid arguments.");
            System.out.println("Usage : dispense <id> <count>");
            return;
        }
        int id, count;
        try {
            id = Integer.parseInt(args.get(0));
            count = Integer.parseInt(args.get(1));
        } catch (NumberFormatException e) {
            System.out.println("ERROR : Invalid arguments.");
            System.out.println("Usage : dispense <id> <count>");
            return;
        }
        super.execute(args);
        controller.dispensePill(id, count);
    }
}
