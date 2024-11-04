package model.data;

import java.util.HashMap;

public class PillSets {
    public final HashMap<String, PillSet> pillSets;
    public PillSets() {
        pillSets = new HashMap<>();
    }
    // !note! id が被ったとき確かめなくてよい？？
    public void newPillSet(String id) {
        if (id.matches("[0-9]+")) {
            System.out.println("ERROR : ID cannot consist of numbers only.");
            return;
        }
        else if (!id.matches("[a-zA-Z0-9]+")) {
            System.out.println("ERROR : Invalid character in ID.");
            return;
        }
        else if (pillSets.containsKey(id)) {
            System.out.println("ERROR : ID already exists.");
            return;
        }
        pillSets.put(id, new PillSet(id));
    }
    public PillSet getPillSet(String id) {
        return pillSets.get(id);
    }
    public void editPillSet(String id, int pillId, int count) {
        PillSet pillSet = pillSets.get(id);
        if (pillSet == null) {
            System.out.println("ERROR : No such pill set.");
            return;
        }
        pillSet.setCount(pillId, count);
    }
    public void removePillSet(String id) {
        PillSet check = pillSets.remove(id);
        if (check == null) {
            System.out.println("ERROR : No such pill set.");
        }
    }
    public void printPillSets() {
        for (String id : pillSets.keySet()) {
            System.out.println(id);
            PillSet pillSet = pillSets.get(id);
            for (int i = 1; i <= pillSet.PILLCOUNT; i++) {
                System.out.println(" pill  " + i + " : " + pillSet.getCount(i));
            }
        }
    }
}
