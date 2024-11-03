package controller.cli.cmd;

import controller.Controller;
import model.data.PillSets;

import java.util.List;
import java.util.Objects;

public class PillSetCmd extends Cmd {
    public PillSetCmd(String name, Controller controller) {
        super(name, controller);
    }

    // pillset
    // pillset add <pillset id>
    // pillset edit <pillset id> <pillid> <count>
    // pillset remove <pillset id>
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        boolean err = false;
        if (args.isEmpty()) {
            PillSets.printPillSets();
            return;
        }
        if (Objects.equals(args.get(0), "add")) {
            if (args.size() == 2) {
                PillSets.newPillSet(args.get(1));
            } else {
                err = true;
            }
        }
        if (Objects.equals(args.get(0), "edit")) {
            if (args.size() == 4) {
                int id, count;
                try {
                    id = Integer.parseInt(args.get(2));
                    count = Integer.parseInt(args.get(3));
                    PillSets.editPillSet(args.get(1), id, count);
                } catch (NumberFormatException e) {
                    err = true;
                }
            } else {
                err = true;
            }
        }
        if (Objects.equals(args.get(0), "remove")) {
            if (args.size() == 2) {
                PillSets.removePillSet(args.get(1));
            } else {
                err = true;
            }
        }
        if (err) {
            System.out.println("ERROR : Invalid arguments.");
            System.out.println("Usage : pillset add <pillset id>");
            System.out.println("Usage : pillset edit <pillset id> <pillid> <count>");
            System.out.println("Usage : pillset remove <pillset id>");
        }
    }
}
