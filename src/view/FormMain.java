package view;

import controller.Controller;
import model.Model;
import model.data.Pair;
import model.history.Intake;

import javax.swing.*;
import java.util.List;

public class FormMain extends JFrame {
    private JTextArea textAreaHistory;
    private JButton button1;
    private JPanel panelMain;
    private JPanel panelPillSet;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JSpinner spinner3;
    private JSpinner spinner4;

    public FormMain() {
        super("Main");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        this.setSize(800 , 800);
        this.setVisible(true);
    }
    public void refresh(Model model) {
        List<Intake> intake = model.history.getHistory();
        textAreaHistory.setText("");
        for (Intake i : intake) {
            textAreaHistory.append("Time : " + i.getTime() + "\n");
            List<Pair<Integer, Integer>> p = i.getPills();
            for (Pair<Integer, Integer> pair : p) {
                textAreaHistory.append("  Pill " + pair.getFirst() + " : count " + pair.getSecond() + "\n");
            }
        }
    }
}
