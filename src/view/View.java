package view;

import controller.Controller;

import javax.swing.*;

public class View {
    private final Controller controller;
    public FormMain formMain;
    public View(Controller controller) {
        this.controller = controller;
        SwingUtilities.invokeLater(() -> {
            FormMain dispenser = new FormMain(controller);
            dispenser.setVisible(true);
        });
    }
}
