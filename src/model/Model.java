package model;

import controller.Controller;
import model.alarm.AlarmManager;
import model.data.PillSets;
import model.pilltracking.PillTrackingManager;
import model.server.ServerManager;

public class Model {
    private final Controller controller;
    public final PillSets pillSets;
    public final ServerManager server;
    public final AlarmManager alarm;
    public final PillTrackingManager pillTracker;
    public Model(int port, Controller controller) {
        System.out.println("Model created");
        this.controller = controller;
        pillSets = new PillSets();
        pillTracker = new PillTrackingManager();
        server = new ServerManager(port, pillSets, pillTracker);
        alarm = new AlarmManager(server);
    }
    public void stop() {
        server.stop();
        alarm.shutdown();
    }

}
