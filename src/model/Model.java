package model;

import controller.Controller;
import model.alarm.AlarmManager;
import model.data.PillSets;
import model.history.PillHistory;
import model.pilltracker.PillTracker;
import model.server.ServerManager;

public class Model {
    private final Controller controller;
    public final PillSets pillSets;
    public final PillTracker pillTracker;
    public final ServerManager server;
    public final AlarmManager alarm;
    public final PillHistory history;
    public Model(int port, Controller controller) {
        System.out.println("Model created");
        this.controller = controller;
        pillSets = new PillSets();
        history = new PillHistory();
        pillTracker = new PillTracker(history, controller);
        server = new ServerManager(port, pillSets, pillTracker, this);
        alarm = new AlarmManager(server);
    }
    public void stop() {
        server.stop();
        alarm.shutdown();
    }
}
