package controller.cli;

import controller.cli.cmd.ICmd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CmdRegistry {
    private final Map<String, ICmd> cmds = new HashMap<>();

    protected void registerCmd(String name, ICmd cmd) {
        cmds.put(name, cmd);
    }

    public void executeCmd(String name, List<String> args) {
        ICmd cmd = cmds.get(name);
        if (cmd == null) {
            System.out.println("Unknown command: " + name);
            return;
        }
        cmd.execute(args);
    }
}
