package model.history;

import model.data.Pair;

import java.time.LocalDateTime;
import java.util.List;

public class Intake {
    private final LocalDateTime time;
    private final List<Pair<Integer, Integer>> pills;  // pillId, count
    public Intake(LocalDateTime time, List<Pair<Integer, Integer>> pills) {
        this.time = time;
        this.pills = pills;
    }
    public Intake(List<Pair<Integer, Integer>> pills) {
        this(LocalDateTime.now(), pills);
    }
    public LocalDateTime getTime() {
        return time;
    }
    public List<Pair<Integer, Integer>> getPills() {
        return pills;
    }
    public void print() {
        System.out.println("Time : " + time);
        for (Pair<Integer, Integer> pill : pills) {
            System.out.println("pillId : " + pill.getFirst() + ", count : " + pill.getSecond());
        }
    }
}
