package model;

import controller.Controller;
import model.alarm.AlarmManager;
import model.data.PillSets;
import model.server.ServerManager;

public class Model {
    private final Controller controller;
    public final PillSets pillSets;
    public final ServerManager server;
    public final AlarmManager alarm;
    public Model(int port, Controller controller) {
        System.out.println("Model created");
        this.controller = controller;
        pillSets = new PillSets();
        server = new ServerManager(port, pillSets);
        alarm = new AlarmManager(server);
    }
    public void stop() {
        server.stop();
        alarm.shutdown();
    }

}
