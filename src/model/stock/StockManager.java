package model.stock;

import controller.Controller;
import view.View;

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
    public void setStock(int id, int count, View view) {
        if (count >= 100) {
            System.out.println("ERROR : Stock exceeds limit.");
            setStock(id, 99, view);
            return;
        }
        stock.put(id, count);
        if (view != null) {
            view.formMain.updateStockAmount(id, stock.get(id));
        }
    }
    public void addStock(int id, int count, View view) {
        setStock(id, stock.get(id) + count, view);
    }
    public void removeStock(int id, int count, View view) {
        if (stock.get(id) < count) {
            System.out.println("ERROR : Not enough stock.");
            setStock(id, 0, view);
            return;
        }
        setStock(id, stock.get(id) - count, view);
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
