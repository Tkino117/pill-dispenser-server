package model;

import controller.Controller;
import model.data.PillSet;
import model.data.PillSets;

public class ServerManager {
    private int port;
    private Server server;
    private PillSets pillSets;
    public ServerManager(int port, PillSets pillSets) {
        this.port = port;
        this.pillSets = pillSets;
        server = new Server(port);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
    // for server
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
        server = new Server(port);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
    public void serverRestart() {
        serverRestart(port);
    }
    public void stop() {
        server.stop();
    }
    public void sendMessage(String message) {
        server.sendMessage(message);
    }
    public void dispensePill(int pillId, int count) {
        System.out.println("Dispensing pill " + pillId + " count " + count);
        sendMessage("dispense " + pillId + " " + count);
    }
    public void dispensePillSet(String pillSetId) {
        PillSet pillSet = pillSets.getPillSet(pillSetId);
        if (pillSet == null) {
            System.out.println("ERROR : Pill set not found.");
            return;
        }
        dispensePillSet(pillSet);
    }
    public void dispensePillSet(PillSet pillSet) {
        System.out.println("Dispensing pill set : " + pillSet.getId());
        for (int i = 0; i < pillSet.PILLCOUNT; i++) {
            sendMessage("dispense " + (i + 1) + " " + pillSet.getCount(i + 1));
        }
    }
}
