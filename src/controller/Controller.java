package controller;

import controller.cmd.CLI;
import model.Server;

public class Controller {
    public int port;
    public Server server;
    public final CLI cli;
    public Controller(int port) {
        System.out.println("Controller created");
        this.port = port;
        cli = new CLI(this);
        server = new Server(port, cli);
        Thread serverThread = new Thread(server);
        Thread cliThread = new Thread(cli);
        serverThread.start();
        cliThread.start();
    }

    public void serverRestart(int port) {
        this.port = port;
        server.stop();
        try {
            Thread.sleep(500);
            System.out.println("wait...");
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server = new Server(port, cli);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
    public void serverRestart() {
        serverRestart(port);
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
        sendMessage("dispense " + pillId + " " + count);
    }
 }
