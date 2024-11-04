package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class TestCmd extends Cmd {
    public TestCmd(String name, Controller controller) {
        super(name, controller);
    }

    // test
    @Override
    public void execute(List<String> args) {
        super.execute(args);
    }
}
