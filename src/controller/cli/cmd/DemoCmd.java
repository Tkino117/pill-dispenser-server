package controller.cli.cmd;

import controller.Controller;

import java.util.List;
import java.util.Objects;

public class DemoCmd extends Cmd {
    public DemoCmd(String name, Controller controller) {
        super(name, controller);
    }

    // demo
    // demo sch
    // demo debug
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        if (args.isEmpty()) {
            System.out.println("made demo pillset");
            controller.cli.execute("pillset add pillset1");
            controller.cli.execute("pillset add pillset2");
            controller.cli.execute("pillset add pillset3");
            controller.cli.execute("pillset edit pillset1 1 2");
            controller.cli.execute("pillset edit pillset1 2 1");
            controller.cli.execute("pillset edit pillset2 1 2");
            controller.cli.execute("pillset edit pillset3 1 2");
            controller.cli.execute("pillset edit pillset3 2 1");
            controller.cli.execute("pillset edit pillset3 3 1");
        }
        else if (Objects.equals(args.get(0), "sch") || Objects.equals(args.get(0), "schedule")) {
            System.out.println("made demo schedule");
            controller.cli.execute("schedule once s_1 pillset1 60");
            controller.cli.execute("schedule repeat sc_1 pillset1 2 30");
            controller.cli.execute("schedule repeat sc_2 pillset2 12 30");
            controller.cli.execute("schedule repeat sc_3 pillset3 22 30");
        }
        else if (Objects.equals(args.get(0), "debug")) {
            System.out.println("Start to execute all commands for debugging. Are you sure? (y/n)");
            String input = controller.cli.scanner.nextLine();
            if (!Objects.equals(input, "yes") && !Objects.equals(input, "y")) {
                System.out.println("Canceled.");
                return;
            }
            try {
                controller.cli.execute("restart");
                Thread.sleep(6000);
                controller.cli.execute("demo");
                Thread.sleep(500);
                controller.cli.execute("send This is a test message for debug.");
                controller.cli.execute("dis 1 4");
                controller.cli.execute("dis 2 1");
                controller.cli.execute("dis 3 1");
                System.out.println("============ ERROR debug ===========");
                controller.cli.execute("dis 4 1");
                controller.cli.execute("dis 0 1");
                controller.cli.execute("track");
                controller.cli.execute("take");
                controller.cli.execute("track");
                System.out.println("====================================");
                controller.cli.execute("dis morning");
                controller.cli.execute("pillset edit morning 1 1");
                controller.cli.execute("pillset remove morning");
                controller.cli.execute("pillset add morning");
                controller.cli.execute("pillset edit morning 1 2");
                controller.cli.execute("pillset");
                System.out.println("============ ERROR debug ===========");
                controller.cli.execute("pillset edit mxxxxxx 1 1");
                controller.cli.execute("pillset edit morning 4 1");
                controller.cli.execute("pillset remove mxxxxxx");
                controller.cli.execute("pillset add morning");
                System.out.println("====================================");
                controller.cli.execute("demo sch");
                controller.cli.execute("schedule edit once s_morning morning 10");
                controller.cli.execute("schedule edit repeat sr_morning morning 1 4");
                controller.cli.execute("schedule edit repeat sr_afternoon afternoon 2 4");
                controller.cli.execute("schedule edit repeat sr_evening evening 3 4");
                controller.cli.execute("schedule");
                Thread.sleep(12000);
                controller.cli.execute("schedule remove sr_morning");
                controller.cli.execute("schedule remove sr_afternoon");
                controller.cli.execute("schedule remove sr_evening");
                controller.cli.execute("schedule");
                controller.cli.execute("take");
                controller.cli.execute("history");
                System.out.println("============ DEBUG FINISHED ===========");
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }
        }

    }
}
