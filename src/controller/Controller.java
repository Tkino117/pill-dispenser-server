package controller;

import controller.cmd.CLI;
import model.Server;

public class Controller {
    private final Server server;
    private final CLI cli;
    public Controller(int port) {
        System.out.println("Controller created");
        server = new Server(port);
        cli = new CLI();
        Thread serverThread = new Thread(server);
        Thread cliThread = new Thread(cli);
        serverThread.start();
        cliThread.start();
    }

    public void sendMessage(String message) {
        server.sendMessage(message);
    }
}
