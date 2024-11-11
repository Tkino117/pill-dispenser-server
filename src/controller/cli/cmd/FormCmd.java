package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class FormCmd extends Cmd {
    public FormCmd(String name, Controller controller) {
        super(name, controller);
    }

    // test
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        if (args.size() == 1 && (args.get(0).equals("refresh") || args.get(0).equals("r"))) {
            controller.view.refresh();
        } else {
            System.out.println("フォームの使い方が間違っています");
        }
    }
}
