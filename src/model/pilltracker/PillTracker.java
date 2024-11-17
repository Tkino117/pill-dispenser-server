package model.pilltracker;

import controller.Controller;
import model.data.Pair;
import model.history.PillHistory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PillTracker {
    private final PillHistory history;
    private final List<Pair<Integer, Integer>> lastPill;  // pillId, count
    private final Controller controller;
    public PillTracker(PillHistory history, Controller controller) {
        this.history = history;
        this.controller = controller;
        lastPill = new ArrayList<>();
    }
    public void add(int pillId, int count) {
        if (count > 0)
            lastPill.add(new Pair<>(pillId, count));
    }
    public void clear() {
        lastPill.clear();
    }
    public List<Pair<Integer, Integer>> getAndClear() {
        List<Pair<Integer, Integer>> res = new ArrayList<>(lastPill);
        clear();
        return res;
    }
    public void takePill() {
        if (!lastPill.isEmpty())
            history.add(getAndClear(), controller.view);
    }
    public void takePill(LocalDateTime time) {
        history.add(time, getAndClear(), controller.view);
    }
    public boolean isEmpty() {
        return lastPill.isEmpty();
    }
    public void printLast() {
        System.out.println("Last pill :");
        for (Pair<Integer, Integer> pill : lastPill) {
            System.out.println("pillId : " + pill.getFirst() + ", count : " + pill.getSecond());
        }
    }
}
