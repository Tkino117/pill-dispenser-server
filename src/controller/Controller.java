package controller;

import controller.cmd.CLI;
import model.Server;

public class Controller {
    public final Server server;
    public final CLI cli;
    public Controller(int port) {
        System.out.println("Controller created");
        cli = new CLI(this);
        server = new Server(port, cli);
        Thread serverThread = new Thread(server);
        Thread cliThread = new Thread(cli);
        serverThread.start();
        cliThread.start();
    }
    public void stop() {
        cli.stop();
        server.stop();
    }

    public void sendMessage(String message) {
        server.sendMessage(message);
    }
    public void dispensePill(int pillId, int count) {
        System.out.println("Dispensing pill " + pillId + " count " + count);
        sendMessage("dispense " + pillId + " " + count);
    }
 }
