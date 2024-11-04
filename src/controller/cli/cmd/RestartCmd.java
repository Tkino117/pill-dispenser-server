package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class RestartCmd extends Cmd {
    public RestartCmd(String name, Controller controller) {
        super(name, controller);
    }

    // restart
    // restart <port>
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        System.out.println("Server Restarting...");
        if (!args.isEmpty()) {
            controller.model.server.serverRestart(Integer.parseInt(args.get(0)));
            return;
        }
        controller.model.server.serverRestart();
    }
}
