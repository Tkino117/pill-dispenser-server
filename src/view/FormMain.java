package view;

import controller.Controller;
import model.data.Pair;
import model.history.Intake;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FormMain extends JFrame {
    private final Controller controller;
    public enum TimingType {
        MORNING("朝", "morning", "sc_morning", 9, 15),
        NOON("昼", "afternoon", "sc_afternoon", 12, 30),
        NIGHT("夜", "evening", "sc_evening", 18, 0);

        private final String label;
        private final String pillSetName;
        private final String scheduleID;
        private final int defaultHour;
        private final int defaultMinute;

        TimingType(String label, String pillSetName, String scheduleID, int defaultHour, int defaultMinute) {
            this.label = label;
            this.pillSetName = pillSetName;
            this.scheduleID = scheduleID;
            this.defaultHour = defaultHour;
            this.defaultMinute = defaultMinute;
        }

        public String getLabel() {
            return label;
        }
    }

    // font size
    private final Font baseFont = new Font(Font.DIALOG, Font.BOLD, (int)(12 * 1.2));
    private final Font titleFont = new Font(Font.DIALOG, Font.BOLD, (int)(14 * 1.2));
    // corner radius
    private static final int CORNER_RADIUS = 15;
    // custom components
    private class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private int radius;

        public RoundedPanel(LayoutManager layout, Color bgColor, int radius) {
            super(layout);
            this.backgroundColor = bgColor;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
        }
    }
    private class RoundedButton extends JButton {
        private static final int BUTTON_RADIUS = 15;
        private Color backgroundColor = new Color(240, 240, 240);
        private Color hoverColor = new Color(220, 220, 220);
        private Color pressedColor = new Color(200, 200, 200);
        private boolean isHovered = false;
        private boolean isPressed = false;

        public RoundedButton(String text) {
            super(text);
            setUp();
        }

        private void setUp() {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    isHovered = true;
                    repaint();
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    isHovered = false;
                    isPressed = false;
                    repaint();
                }

                public void mousePressed(java.awt.event.MouseEvent evt) {
                    isPressed = true;
                    repaint();
                }

                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    isPressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isPressed) {
                g2.setColor(pressedColor);
            } else if (isHovered) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(backgroundColor);
            }

            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BUTTON_RADIUS, BUTTON_RADIUS);

            FontMetrics metrics = g2.getFontMetrics(getFont());
            int textWidth = metrics.stringWidth(getText());
            int textHeight = metrics.getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = ((getHeight() - textHeight) / 2) + metrics.getAscent();

            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    private final Map<LocalDate, JPanel> datePanelsMap = new HashMap<>();

    public FormMain(Controller controller) {
        this.controller = controller;
        setTitle("Smart Pill Dispenser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUIFont(new javax.swing.plaf.FontUIResource(baseFont));

        // logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(240, 240, 240));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // logo label
        JLabel logoLabel = new JLabel("Smart Pill Dispenser");
        logoLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
        logoPanel.add(logoLabel);

        // calendar panel
        JScrollPane calendarScrollPane = createCalendarPanel();

        // settings panel
        JPanel settingsPanel = createSettingsPanel();

        // command input panel
        JPanel commandPanel = new JPanel(new BorderLayout());
        commandPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JTextField commandInput = new JTextField();
        commandInput.setFont(baseFont);
        commandInput.addActionListener(e -> {
            String command = commandInput.getText();
            onCommandEntered(command);
            commandInput.setText("");
        });

        commandPanel.add(commandInput, BorderLayout.CENTER);

        // main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // center panel to combine calendar and settings
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(calendarScrollPane, BorderLayout.CENTER);
        centerPanel.add(settingsPanel, BorderLayout.EAST);

        mainPanel.add(logoPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(commandPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setSize(1400, 850);
        setLocationRelativeTo(null);
    }

    // for demo
    public void demoHistory() {
        for (int i = 11; i <= 17; i++) {
            LocalDate date = LocalDate.of(2024, 11, i);
            LocalTime time = LocalTime.of(9, 30, 45);
            int[] amounts = {1, 1, 0}; // 薬1: 2個, 薬2: 1個, 薬3: 3個
            this.addMedicationHistory(date, time, amounts);
        }
        LocalDate date = LocalDate.of(2024, 11, 15);
        LocalTime time = LocalTime.of(12, 30, 0);
        int[] amounts = {2, 3, 4}; // 薬1: 2個, 薬2: 1個, 薬3: 3個
        this.addMedicationHistory(date, time, amounts);
        time = LocalTime.of(18, 30, 0);
        amounts = new int[]{0, 0, 1}; // 薬1: 2個, 薬2: 1個, 薬3: 3個
        this.addMedicationHistory(date, time, amounts);
    }

    // event handlers
    public void onMedicationAmountChanged(TimingType timing, int pillNumber, int newAmount) {
//        System.out.println(String.format(
//                "薬の個数が変更されました - 時間帯: %s, 薬%d: %d個",
//                timing.getLabel(), pillNumber, newAmount
//        ));
        controller.cli.execute(("pillset edit " + timing.pillSetName + " " + pillNumber + " " + newAmount));
    }

    public void onScheduleSettingChanged(TimingType timing, boolean isDaily, int hour, int minute) {
//        System.out.println(String.format(
//                "服用スケジュールが変更されました - 時間帯: %s, 毎日: %b, %02d:%02d",
//                timing.getLabel(), isDaily, hour, minute
//        ));
        if (isDaily) {
            //!!note!! 本来 schedule command をhh:mm対応するべきだった
            controller.model.alarm.cancelTask(timing.scheduleID, false);
            controller.model.alarm.schedulePeriodicTask(timing.scheduleID,
                    controller.model.alarm.toTask(controller.model.pillSets.getPillSet(timing.pillSetName)),
                    LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute)), 86400);
        } else {
            controller.model.alarm.cancelTask(timing.scheduleID, false);
        }
    }

    public void onCommandEntered(String command) {
        controller.cli.execute(command);
    }

    public void onDispenseButtonClicked(TimingType timing) {
        controller.cli.execute("dispense " + timing.pillSetName);
    }

    private void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    // helper
    private void adjustScrollSpeed(JScrollBar scrollBar, int unitIncrement, int blockIncrement) {
        scrollBar.setUnitIncrement(unitIncrement);
        scrollBar.setBlockIncrement(blockIncrement);
    }
    private JPanel findTimeSlotsContainer(JPanel dayPanel) {
        Component centerComponent = ((BorderLayout)dayPanel.getLayout())
                .getLayoutComponent(dayPanel, BorderLayout.CENTER);

        if (centerComponent instanceof JPanel) {
            return (JPanel) centerComponent;
        }
        return null;
    }

    // public methods
    public void addMedicationHistory(LocalDate date, LocalTime time, int[] medicationAmounts) {
        if (medicationAmounts.length != 3) {
            throw new IllegalArgumentException("medicationAmounts must have 3 elements");
        }

        SwingUtilities.invokeLater(() -> {
            JPanel dayPanel = datePanelsMap.get(date);
            if (dayPanel != null) {
                JPanel timeSlotsContainer = findTimeSlotsContainer(dayPanel);
                if (timeSlotsContainer != null) {
                    String timeStr = String.format("%02d時%02d分%02d秒",
                            time.getHour(), time.getMinute(), time.getSecond());

                    JPanel timeSlot = createTimeSlotPanel(timeStr,
                            "薬1: " + medicationAmounts[0] + "個",
                            "薬2: " + medicationAmounts[1] + "個",
                            "薬3: " + medicationAmounts[2] + "個");

                    timeSlot.setAlignmentX(Component.LEFT_ALIGNMENT);

                    int componentCount = timeSlotsContainer.getComponentCount();
                    if (componentCount > 0) {
                        // VerticalGlueの直前に追加
                        timeSlotsContainer.add(timeSlot, componentCount - 1);
                        timeSlotsContainer.add(Box.createRigidArea(new Dimension(0, 5)), componentCount - 1);
                    }

                    timeSlotsContainer.revalidate();
                    timeSlotsContainer.repaint();
                }
            }
        });
    }
    public void addMedicationHistory(Intake intake) {
        int[] pills = {0, 0, 0};
        for (Pair<Integer, Integer> i : intake.getPills()) {
            pills[i.getFirst() - 1] += i.getSecond();
        }
        addMedicationHistory(intake.getTime().toLocalDate(), intake.getTime().toLocalTime(), pills);
    }

    private JScrollPane createCalendarPanel() {
        LocalDate startDate = LocalDate.of(2024, 11, 10);
        LocalDate endDate = LocalDate.of(2024, 11, 30);
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

        int rows = (int) Math.ceil(daysBetween / 7.0);
        JPanel panel = new JPanel(new GridLayout(rows, 7, 5, 5));
        panel.setBackground(new Color(240, 240, 240));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");

        JScrollPane mainScrollPane = new JScrollPane(panel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        adjustScrollSpeed(mainScrollPane.getVerticalScrollBar(), 50, 30);
        adjustScrollSpeed(mainScrollPane.getHorizontalScrollBar(), 50, 30);

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            JPanel dayPanel = createDayPanel(currentDate.format(formatter));
            panel.add(dayPanel);
            datePanelsMap.put(currentDate, dayPanel);
            currentDate = currentDate.plusDays(1);
        }

        return mainScrollPane;
    }

    private JPanel createDayPanel(String date) {
        RoundedPanel panel = new RoundedPanel(new BorderLayout(), Color.WHITE, CORNER_RADIUS);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(titleFont);
        dateLabel.setHorizontalAlignment(JLabel.CENTER);
        dateLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(dateLabel, BorderLayout.NORTH);

        JPanel timeSlots = new JPanel();
        timeSlots.setLayout(new BoxLayout(timeSlots, BoxLayout.Y_AXIS));
        timeSlots.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        timeSlots.setOpaque(false);

        timeSlots.add(Box.createVerticalGlue());

        panel.add(timeSlots, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTimeSlotPanel(String time, String... medications) {
        RoundedPanel borderedPanel = new RoundedPanel(new BorderLayout(), new Color(245, 245, 245), CORNER_RADIUS);
        borderedPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        borderedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, borderedPanel.getPreferredSize().height));

        JPanel contentPanel = new JPanel(new GridLayout(medications.length + 1, 1, 2, 2));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(baseFont);
        contentPanel.add(timeLabel);

        for (String medication : medications) {
            JLabel medLabel = new JLabel(medication);
            medLabel.setFont(baseFont);
            contentPanel.add(medLabel);
        }

        borderedPanel.add(contentPanel, BorderLayout.CENTER);
        return borderedPanel;
    }

    private JPanel createSettingsPanel() {
        // Create container panel
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setPreferredSize(new Dimension(330, 0));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        containerPanel.setOpaque(false);

        // Create panel for timing settings
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setOpaque(false);

        // Add timing settings
        for (TimingType timing : TimingType.values()) {
            int p1 = controller.model.pillSets.getPillSet(timing.pillSetName).getCount(1);
            int p2 = controller.model.pillSets.getPillSet(timing.pillSetName).getCount(2);
            int p3 = controller.model.pillSets.getPillSet(timing.pillSetName).getCount(3);

            JPanel panel = createTimeSetting(timing.getLabel(),
                    new int[]{p1, p2, p3}, timing.defaultHour, timing.defaultMinute);
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
            settingsPanel.add(panel);
            settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Adjust scroll speed
        adjustScrollSpeed(scrollPane.getVerticalScrollBar(), 16, 50);

        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private JPanel createTimeSetting(String time, int[] amounts, int hour, int minute) {
        // Get outer panel color based on timing
        Color panelColor = getTimingColor(time);

        // Create outer colored panel
        RoundedPanel outerPanel = new RoundedPanel(new BorderLayout(), panelColor, CORNER_RADIUS);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, outerPanel.getPreferredSize().height));

        // Get TimingType
        TimingType timing = Arrays.stream(TimingType.values())
                .filter(t -> t.getLabel().equals(time))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid timing: " + time));

        // Title label (white text)
        JLabel titleLabel = new JLabel(time);
        titleLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        outerPanel.add(titleLabel, BorderLayout.NORTH);

        // Inner white panel
        RoundedPanel innerPanel = new RoundedPanel(new BorderLayout(), Color.WHITE, CORNER_RADIUS);
        innerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Medication settings
        JPanel medicationPanel = new JPanel(new GridLayout(3, 1, 5, 0));
        medicationPanel.setOpaque(false);

        JSpinner[] spinners = new JSpinner[3];
        for (int i = 0; i < 3; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.setOpaque(false);

            JLabel label = new JLabel("薬" + (i + 1) + ":");
            label.setFont(baseFont);
            row.add(label);

            spinners[i] = new JSpinner(new SpinnerNumberModel(amounts[i], 0, 10, 1));
            spinners[i].setFont(baseFont);
            spinners[i].setPreferredSize(new Dimension(80, spinners[i].getPreferredSize().height));

            JLabel unitLabel = new JLabel("個");
            unitLabel.setFont(baseFont);

            // Add spinner listener
            final int pillNumber = i + 1;
            spinners[i].addChangeListener(e -> {
                int newAmount = (Integer) spinners[pillNumber - 1].getValue();
                onMedicationAmountChanged(timing, pillNumber, newAmount);
            });

            row.add(spinners[i]);
            row.add(unitLabel);
            medicationPanel.add(row);
        }

        // Time settings panel
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setOpaque(false);

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.setOpaque(false);
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox dailyCheck = new JCheckBox("毎日");
        dailyCheck.setFont(baseFont);
        dailyCheck.setOpaque(false);

        JSpinner hourSpinner = new JSpinner(new SpinnerNumberModel(hour, 0, 23, 1));
        hourSpinner.setFont(baseFont);
        JSpinner minuteSpinner = new JSpinner(new SpinnerNumberModel(minute, 0, 59, 1));
        minuteSpinner.setFont(baseFont);

        JLabel hourLabel = new JLabel("時");
        hourLabel.setFont(baseFont);
        JLabel minuteLabel = new JLabel("分に服用");
        minuteLabel.setFont(baseFont);

        // Add time setting listeners
        ChangeListener scheduleChangeListener = e -> {
            boolean isDaily = dailyCheck.isSelected();
            int h = (Integer) hourSpinner.getValue();
            int m = (Integer) minuteSpinner.getValue();
            onScheduleSettingChanged(timing, isDaily, h, m);
        };

        hourSpinner.addChangeListener(scheduleChangeListener);
        minuteSpinner.addChangeListener(scheduleChangeListener);
        dailyCheck.addActionListener(e -> scheduleChangeListener.stateChanged(null));

        timePanel.add(dailyCheck);
        timePanel.add(hourSpinner);
        timePanel.add(hourLabel);
        timePanel.add(minuteSpinner);
        timePanel.add(minuteLabel);

        // Dispense button panel
        JPanel dispensePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dispensePanel.setOpaque(false);
        dispensePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton dispenseButton = new RoundedButton("手動排出");
        dispenseButton.setFont(baseFont);
        dispenseButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        dispenseButton.addActionListener(e -> onDispenseButtonClicked(timing));
        dispensePanel.add(dispenseButton);

        // Combine panels
        southPanel.add(timePanel);
        southPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        southPanel.add(dispensePanel);

        innerPanel.add(medicationPanel, BorderLayout.CENTER);
        innerPanel.add(southPanel, BorderLayout.SOUTH);

        outerPanel.add(innerPanel, BorderLayout.CENTER);

        return outerPanel;
    }

    private Color getTimingColor(String time) {
        switch (time) {
            case "朝": return new Color(255, 69, 58);  // Morning red
            case "昼": return new Color(255, 159, 10); // Noon orange
            case "夜": return new Color(94, 92, 230);  // Night blue
            default: return Color.GRAY;
        }
    }

}