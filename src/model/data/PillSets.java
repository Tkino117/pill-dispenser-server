package model.data;

import java.util.HashMap;

public class PillSets {
    public static final HashMap<String, PillSet> PILL_SETS;
    static {
        PILL_SETS = new HashMap<>();
    }
    public static PillSet getPillSet(String id) {
        return PILL_SETS.get(id);
    }
    // !note! id が被ったとき確かめなくてよい？？
    public static void newPillSet(String id) {
        if (id.matches("[0-9]+")) {
            System.out.println("ERROR : ID cannot consist of numbers only.");
            return;
        }
        else if (!id.matches("[a-zA-Z0-9]+")) {
            System.out.println("ERROR : Invalid character in ID.");
            return;
        }
        else if (PILL_SETS.containsKey(id)) {
            System.out.println("ERROR : ID already exists.");
            return;
        }
        PILL_SETS.put(id, new PillSet());
    }
    public static void editPillSet(String id, int pillId, int count) {
        PillSet pillSet = PILL_SETS.get(id);
        if (pillSet == null) {
            System.out.println("ERROR : No such pill set.");
            return;
        }
        pillSet.setCount(pillId, count);
    }
    public static void removePillSet(String id) {
        PillSet check = PILL_SETS.remove(id);
        if (check == null) {
            System.out.println("ERROR : No such pill set.");
        }
    }
    public static void printPillSets() {
        for (String id : PILL_SETS.keySet()) {
            System.out.println(id);
            PillSet pillSet = PILL_SETS.get(id);
            for (int i = 1; i <= pillSet.PILLCOUNT; i++) {
                System.out.println(" pill  " + i + " : " + pillSet.getCount(i));
            }
        }
    }
}
