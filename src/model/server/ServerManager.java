package model.server;

import model.Model;
import model.data.PillSet;
import model.data.PillSets;
import model.pilltracker.PillTracker;

import javax.swing.*;

public class ServerManager {
    private int port;
    private Server server;
    private final PillSets pillSets;
    private final PillTracker pillTracker;
    private final MessageExecutor executor;
    public ServerManager(int port, PillSets pillSets, PillTracker pillTracker, Model model) {
        this.port = port;
        this.pillSets = pillSets;
        this.pillTracker = pillTracker;
        executor = new MessageExecutor(model);
        server = new Server(port, this);
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
        server = new Server(port, this);
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
    public void dispensePill(int pillId, int count, boolean showMessage) {
        if (pillId < 1 || pillId > 3) {
            System.out.println("ERROR : Invalid pill id.");
            return;
        }
        if (showMessage)
            System.out.println("Dispensing pill " + pillId + " count " + count);
        sendMessage("dispense " + pillId + " " + count);
        pillTracker.add(pillId, count);
    }
    public void dispensePill(int pillId, int count) {
        dispensePill(pillId, count, true);
    }
    public void dispensePillSet(PillSet pillSet) {
        if (pillTracker.isEmpty()) {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "薬の飲み残しがあります。排出しますか？",
                    "注意",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.NO_OPTION) {
                System.out.println("dispensing canceled");
                return;
            }
        }
        System.out.println("Dispensing pill set : " + pillSet.getId());
        for (int i = 0; i < pillSet.PILLCOUNT; i++) {
            dispensePill(i + 1, pillSet.getCount(i + 1), false);
        }
    }
    public void dispensePillSet(String pillSetId) {
        PillSet pillSet = pillSets.getPillSet(pillSetId);
        if (pillSet == null) {
            System.out.println("ERROR : Pill set not found.");
            return;
        }
        dispensePillSet(pillSet);
    }
    public void executeMessage(String message) {
        executor.execute(message);
    }
}
