package controller;

import controller.cli.CLI;
import model.Model;
import view.View;

public class Controller {
    public int port;
    public final CLI cli;
    public View view;
    public final Model model;
    public Controller(int port) {
        System.out.println("Controller created");
        this.port = port;
        cli = new CLI(this);
        view = null;
        model = new Model(port, this);
        Thread cliThread = new Thread(cli);
        cliThread.start();
        makeView();
    }

    public void makeView() {
        cli.execute("pillset add morning");
        cli.execute("pillset add afternoon");
        cli.execute("pillset add evening");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        view = new View(this);
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
