package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class HelpCmd extends Cmd {
    public HelpCmd(String name, Controller controller) {
        super(name, controller);
    }

    // help
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        System.out.println("一覧があるので notion をみてね");
    }
}
