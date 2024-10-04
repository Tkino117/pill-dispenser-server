package controller.cmd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdRegistry {
    private final Map<String, Cmd> cmds = new HashMap<>();

    protected void registerCmd(String name, Cmd cmd) {
        cmds.put(name, cmd);
    }

    public void executeCmd(String name, List<String> args) {
        Cmd cmd = cmds.get(name);
        if (cmd == null) {
            System.out.println("Unknown command: " + name);
            return;
        }
        cmd.execute(args);
    }
}
