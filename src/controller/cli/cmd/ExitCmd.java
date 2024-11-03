package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class ExitCmd extends Cmd {
    public ExitCmd(String name, Controller controller) {
        super(name, controller);
    }

    @Override
    public void execute(List<String> args) {
        System.out.println("Exiting...");
        controller.stop();
    }
}
