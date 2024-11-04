package model;

import model.data.Pair;
import model.data.PillSet;

import java.util.ArrayList;
import java.util.List;

public class PillTrackingManager {
    private final List<PillSet> lastPillSet;
    private final List<Pair<Integer, Integer>> lastPill;  // pillId, count
    public PillTrackingManager() {
        lastPillSet = new ArrayList<>();
        lastPill = new ArrayList<>();
    }

    making
}
