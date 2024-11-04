package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class ExitCmd extends Cmd {
    public ExitCmd(String name, Controller controller) {
        super(name, controller);
    }

    // exit
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        System.out.println("Exiting...");
        controller.stop();
    }
}
