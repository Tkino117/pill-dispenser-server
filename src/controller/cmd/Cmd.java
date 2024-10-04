package controller.cmd;

import java.util.List;

public interface Cmd {
    void execute(List<String> args);
}
