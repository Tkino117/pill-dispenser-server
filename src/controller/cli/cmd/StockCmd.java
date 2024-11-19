package controller.cli.cmd;

import controller.Controller;

import java.util.List;
import java.util.Objects;

public class StockCmd extends Cmd {
    public StockCmd(String name, Controller controller) {
        super(name, controller);
    }

    @Override
    public void execute(List<String> args) {
        super.execute(args);
        boolean err = false;
        if (args.isEmpty()) {
            // print stock
            controller.model.stockManager.printStock();
        }
        else if (args.size() == 3) {
            int id, count;
            try {
                id = Integer.parseInt(args.get(1));
                count = Integer.parseInt(args.get(2));
                if (Objects.equals(args.get(0), "set")) {
                    controller.model.stockManager.setStock(id, count);
                }
                else if (Objects.equals(args.get(0), "add")) {
                    controller.model.stockManager.addStock(id, count);
                }
                else if (Objects.equals(args.get(0), "remove") || Objects.equals(args.get(0), "rm")) {
                    controller.model.stockManager.removeStock(id, count);
                }
                else {
                    err = true;
                }
            } catch (NumberFormatException e) {
                err = true;
            }
        }
        else {
            err = true;
        }
        if (err) {
            System.out.println("ERROR : Invalid arguments.");
            System.out.println("Usage : stock <set/add/remove> <id> <count>");
        }
    }
}
