package controller.cli.cmd;

import controller.Controller;

import java.util.List;

public class ScheduleCmd extends Cmd {
    public ScheduleCmd(String name, Controller controller) {
        super(name, controller);
    }

    // schedule once <schedule id> <pillset id> <delay_sec / time>
    // schedule repeat <schedule id> <pillset id> <delay_sec / time> <interval_sec / time>
    // schedule edit <schedule id> <pillset id> <time>
    // schedule edit <schedule id> <pillset id> <delay_sec / time> <interval_sec / time>
    // schedule remove <schedule id>
    @Override
    public void execute(List<String> args) {
        super.execute(args);
        なかみつくるぞー notion にまとめおわってないのでそっちもね
    }
}
