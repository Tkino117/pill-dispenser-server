package model.history;

import model.data.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PillHistory {
    private List<Intake> history;
    public List<Intake> getHistory() {
        return history;
    }
    public PillHistory() {
        this.history = new ArrayList<>();
    }
    public void add(Intake intake) {
        history.add(intake);
    }
    public void add(LocalDateTime time, List<Pair<Integer, Integer>> pills) {
        add(new Intake(time, pills));
    }
    public void add(List<Pair<Integer, Integer>> pills) {
        add(new Intake(pills));
    }
    public void printHistroy() {
        for (Intake intake : history) {
            intake.print();
        }
    }
}
