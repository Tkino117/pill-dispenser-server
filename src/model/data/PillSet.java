package model.data;

import java.util.HashMap;

public class PillSet {
    public final int COUNT = 3;
    private final HashMap<Integer, Integer> pills;  // id, count (id = 1, 2, 3)
    public PillSet() {
        pills = new HashMap<>();
        for (int i = 0; i < COUNT; i++) {
            pills.put(i + 1, 0);
        }
    }
    public int getCount(int id) {
        return pills.get(id);
    }
    public void setCount(int id, int count) {
        if (count < 0) {
            System.out.println("ERROR : Invalid count.");
            return;
        }
        else if (count > 10) {
            System.out.println("ERROR : Count exceeds limit.");
            return;
        }
        if (id < 1 || id > 3) {
            System.out.println("ERROR : Invalid id.");
            return;
        }
        pills.put(id, count);
    }
}
