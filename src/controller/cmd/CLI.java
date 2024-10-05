package controller.cmd;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// How to make command
// 1. Create a class that implements Cmd interface.
// 2. register the command to registry in CLI() method.

// How to run command
// 1. make a new instance of CLI
// 2. if you want to use CLI, call run() method
// 3. if you want to execute a command, call execute() method

public class CLI {
    private final CmdRegistry registry;
    public CLI() {
        registry = new CmdRegistry();
        registry.registerCmd("test", new TestCmd());
    }
    public void run() {
        System.out.println("CLI is running");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
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


    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.run();
    }
}
