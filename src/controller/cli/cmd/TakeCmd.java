package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class TakeCmd extends Cmd {
    public TakeCmd(String name, Controller controller) {
        super(name, controller);
    }

    // take
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        controller.model.server.executeMessage("take pill");
    }
}
