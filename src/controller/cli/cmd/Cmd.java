package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public abstract class Cmd implements ICmd {
    private final String name;
    protected Controller controller;
    public Cmd(String name, Controller controller) {
        this.name = name;
        this.controller = controller;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void execute(List<String> args) {
        System.out.println(name + " command executed with args : " + args);
    }
}
