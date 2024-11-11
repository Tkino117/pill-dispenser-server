package controller.cli;

import controller.Controller;
import controller.cli.cmd.*;

import java.util.*;

// How to make command
// 1. Create a class that implements Cmd interface.
// 2. register the command to registry in CLI() method.

// How to run
// 1. make a new instance of CLI
// 2. if you want to use CLI, call run() method
// 3. if you want to execute a command, call execute() method

// How to run in multi thread
// 1. make a new instance of CLI
// 2. make a new thread with CLI instance ( Thread t = new Thread(cli) )
// 3. start the thread ( t.start() )

public class CLI implements Runnable {
    private final CmdRegistry registry;
    public final Map<String, String> omissionMap;
    private volatile boolean running = true;
    public final Scanner scanner;
    public CLI(Controller controller) {
        scanner = new Scanner(System.in);
        registry = new CmdRegistry();
        omissionMap = new HashMap<>();
        // register commands here!
        registry.registerCmd("demo", new DemoCmd("demo", controller));
        registry.registerCmd("dispense", new DispenseCmd("dispense", controller));
        registry.registerCmd("exit", new ExitCmd("exit", controller));
        registry.registerCmd("form", new FormCmd("form", controller));
        registry.registerCmd("help", new HelpCmd("help", controller));
        registry.registerCmd("history", new HistoryCmd("history", controller));
        registry.registerCmd("pillset", new PillSetCmd("pillset", controller));
        registry.registerCmd("restart", new RestartCmd("restart", controller));
        registry.registerCmd("schedule", new ScheduleCmd("schedule", controller));
        registry.registerCmd("send", new SendCmd("send", controller));
        registry.registerCmd("take", new TakeCmd("take", controller));
        registry.registerCmd("test", new TestCmd("test", controller));
        registry.registerCmd("track", new TrackCmd("track", controller));
        // register omissions here!
        omissionMap.put("dis", "dispense");
        omissionMap.put("q", "exit");
        omissionMap.put("res", "restart");
        omissionMap.put("sch", "schedule");
    }
    @Override
    public void run() {
        System.out.println("CLI is running");
        while (running) {
            String input = scanner.nextLine();
            if (input.matches("") || input.matches(" +")) {
                continue;
            }
            execute(input);
        }
    }
    public void execute(String name, List<String> args) {
        registry.executeCmd(name, args);
    }
    public void execute(String input) {
        List<String> parts = Arrays.asList(input.split(" "));
        String name = parts.get(0);
        if (omissionMap.containsKey(name)) {
            name = omissionMap.get(name);
        }
        List<String> args = parts.subList(1, parts.size());
        execute(name, args);
    }
    public void stop() {
        running = false;
    }
}
