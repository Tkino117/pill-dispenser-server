package controller.cli.cmd;

import controller.Controller;

import java.util.List;
import java.util.Objects;

public class DemoCmd extends Cmd {
    public DemoCmd(String name, Controller controller) {
        super(name, controller);
    }

    @Override
    public void execute(List<String> args) {
        super.execute(args);
        if (args.isEmpty()) {
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
        else if (Objects.equals(args.get(0), "sch") || Objects.equals(args.get(0), "schedule")) {
            controller.cli.execute("schedule once s_morning morning 60");
            controller.cli.execute("schedule repeat sr_morning morning 5 15");
            controller.cli.execute("schedule repeat sr_afternoon afternoon 6 15");
            controller.cli.execute("schedule repeat sr_evening evening 7 15");
        }

    }
}
