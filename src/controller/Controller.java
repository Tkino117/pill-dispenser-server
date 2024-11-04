package controller;

import controller.cli.CLI;
import model.Model;

public class Controller {
    public int port;
    public final CLI cli;
    public final Model model;
    public Controller(int port) {
        System.out.println("Controller created");
        this.port = port;
        cli = new CLI(this);
        model = new Model(port, this);
        Thread cliThread = new Thread(cli);
        cliThread.start();
    }



    public void stop() {
        cli.stop();
        model.stop();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
 }
