package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class AlertCmd extends Cmd {
    public AlertCmd(String name, Controller controller) {
        super(name, controller);
    }

    // test
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        controller.model.server.startAlert();
    }
}
