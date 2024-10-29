package controller.cmd;

import java.util.List;

public interface ICmd {
    String getName();
    void execute(List<String> args);
}
