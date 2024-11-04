package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class SendCmd extends Cmd {
    public SendCmd(String name, Controller controller) {
        super(name, controller);
    }

    @Override
    public void execute(List<String> args) {
        super.execute(args);
        StringBuilder message = new StringBuilder();
        for (String s : args) {
            message.append(" ").append(s);
        }
        controller.model.server.sendMessage(message.toString());
    }
}
