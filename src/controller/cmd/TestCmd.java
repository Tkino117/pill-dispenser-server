package controller.cmd;

import controller.Controller;

import java.util.List;

public class TestCmd extends Cmd {
    public TestCmd(String name, Controller controller) {
        super(name, controller);
    }

    @Override
    public void execute(List<String> args) {
        super.execute(args);
    }
}
