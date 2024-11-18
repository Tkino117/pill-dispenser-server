package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class ViewCmd extends Cmd {
    public ViewCmd(String name, Controller controller) {
        super(name, controller);
    }

    // test
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        if (args.isEmpty()) {
            controller.makeView();
        }
    }
}
