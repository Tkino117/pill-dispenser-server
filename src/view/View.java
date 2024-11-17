package view;

import controller.Controller;
import controller.cli.CLI;

public class View {
    private final Controller controller;
    public SmartPillDispenser formMain;
    public View(Controller controller) {
        this.controller = controller;
        formMain = new SmartPillDispenser();
    }
}
