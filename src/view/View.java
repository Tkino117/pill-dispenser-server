package view;

import controller.Controller;
import controller.cli.CLI;

public class View {
    private final Controller controller;
    public FormMain formMain;
    public View(Controller controller) {
        this.controller = controller;
        formMain = new FormMain();
    }
    public void refresh() {
        formMain.refresh(controller.model);
    }
}
