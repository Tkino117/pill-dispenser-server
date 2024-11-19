package model.stock;

import controller.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockManager {
    private final HashMap<Integer, Integer> stock;
    private final int defaultStock = 20;
    private final Controller controller;
    public StockManager(Controller controller) {
        this.controller = controller;
        stock = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            stock.put(i, defaultStock);
        }
    }
    public void setStock(int id, int count) {
        stock.put(id, count);
    }
    public void addStock(int id, int count) {
        stock.put(id, stock.get(id) + count);
    }
    public void removeStock(int id, int count) {
        stock.put(id, stock.get(id) - count);
    }
    public int getStock(int id) {
        return stock.get(id);
    }
    public void printStock() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("Pill " + i + " : " + stock.get(i));
        }
    }
}
