package model.server;

import model.Model;

import java.util.Objects;

public class MessageExecutor {
    private final Model model;
    public MessageExecutor(Model model) {
        this.model = model;
    }
    public void execute(String message) {
        if (Objects.equals(message, "take pill")) {
            System.out.println("executing in Server");
            model.pillTracker.takePill();
        } else {
            System.out.println("Unknown message : " + message);
        }
    }
}
