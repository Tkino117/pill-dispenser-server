package model.pilltracking;

import model.data.Pair;
import model.data.PillSet;
import model.history.Intake;
import model.history.PillHistory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PillTrackingManager {
    private final PillHistory history;
    private final List<Pair<Integer, Integer>> lastPill;  // pillId, count
    public PillTrackingManager(PillHistory history) {
        this.history = history;
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
        history.add(getAndClear());
    }
    public void takePill(LocalDateTime time) {
        history.add(time, getAndClear());
    }
    public void printLast() {
        System.out.println("Last pill :");
        for (Pair<Integer, Integer> pill : lastPill) {
            System.out.println("pillId : " + pill.getFirst() + ", count : " + pill.getSecond());
        }
    }
}
