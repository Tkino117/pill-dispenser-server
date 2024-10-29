package controller.cmd;

import controller.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
    private final Controller controller;
    private final CmdRegistry registry;
    public CLI(Controller controller) {
        this.controller = controller;
        registry = new CmdRegistry();
        registry.registerCmd("test", new TestCmd("test", controller));
        registry.registerCmd("send", new SendCmd("send", controller));
    }
    @Override
    public void run() {
        System.out.println("CLI is running");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("exit") || input.startsWith("exit ")) {
                break;
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
        List<String> args = parts.subList(1, parts.size());
        execute(name, args);
    }
}
