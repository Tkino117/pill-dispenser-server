package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class DemoCmd extends Cmd {
    public DemoCmd(String name, Controller controller) {
        super(name, controller);
    }

    @Override
    public void execute(List<String> args) {
        super.execute(args);
        controller.cli.execute("pillset add morning");
        controller.cli.execute("pillset add afternoon");
        controller.cli.execute("pillset add evening");
        controller.cli.execute("pillset edit morning 1 2");
        controller.cli.execute("pillset edit morning 2 1");
        controller.cli.execute("pillset edit afternoon 1 2");
        controller.cli.execute("pillset edit evening 1 2");
        controller.cli.execute("pillset edit evening 2 1");
        controller.cli.execute("pillset edit evening 3 1");
    }
}
