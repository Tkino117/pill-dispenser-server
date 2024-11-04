package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class HistoryCmd extends Cmd {
    public HistoryCmd(String name, Controller controller) {
        super(name, controller);
    }

    // history
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        controller.model.history.printHistroy();
    }
}
