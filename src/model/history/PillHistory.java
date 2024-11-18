package model.history;

import model.data.Pair;
import view.View;

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
    public void add(Intake intake, View view) {
        history.add(intake);
        if (view != null) {
            view.formMain.addMedicationHistory(intake);
        }
    }
    public void add(LocalDateTime time, List<Pair<Integer, Integer>> pills, View view) {
        add(new Intake(time, pills), view);
    }
    public void add(List<Pair<Integer, Integer>> pills, View view) {
        add(new Intake(pills), view);
    }
    public void printHistroy() {
        for (Intake intake : history) {
            intake.print();
        }
    }
}
