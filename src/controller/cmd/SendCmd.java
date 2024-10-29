package controller.cmd;

import controller.Controller;

import java.util.List;

public class SendCmd extends Cmd {
    public SendCmd(String name, Controller controller) {
        super(name, controller);
    }

    @Override
    public void execute(List<String> args) {
        super.execute(args);
        controller.sendMessage(args.get(0));
    }
}
