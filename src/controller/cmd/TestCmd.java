package controller.cmd;

import java.util.List;

public class TestCmd implements Cmd{
    @Override
    public void execute(List<String> args) {
        System.out.println("Test command executed with args: " + args);
    }
}
