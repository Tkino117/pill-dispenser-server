package view;

import controller.Controller;
import model.data.Pair;
import model.history.Intake;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;
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

    // FormMainクラスにフィールドを追加
    private final Map<Integer, JLabel> stockLabels = new HashMap<>();

    // font size
    private final Font baseFont = new Font("メイリオ", Font.BOLD, (int)(12 * 1.2));
    private final Font titleFont = new Font("メイリオ", Font.BOLD, (int)(14 * 1.2));
    // corner radius
    private static final int CORNER_RADIUS = 15;
    // custom components
    private class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private int radius;
        private boolean useGradient = false;
        private Color gradientStartColor;
        private Color gradientEndColor;

        public RoundedPanel(LayoutManager layout, Color bgColor, int radius) {
            super(layout);
            this.backgroundColor = bgColor;
            this.radius = radius;
            setOpaque(false);
        }

        // 新しいコンストラクタを追加
        public RoundedPanel(LayoutManager layout, Color bgColor, int radius, boolean useGradient, Color gradientStart, Color gradientEnd) {
            super(layout);
            this.backgroundColor = bgColor;
            this.radius = radius;
            this.useGradient = useGradient;
            this.gradientStartColor = gradientStart;
            this.gradientEndColor = gradientEnd;
            setOpaque(false);
        }

        // paintComponent メソッドを修正
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (useGradient) {
                GradientPaint gradient = new GradientPaint(
                        0, 0, gradientStartColor,
                        getWidth(), getHeight(), gradientEndColor
                );
                g2.setPaint(gradient);
            } else {
                g2.setColor(backgroundColor);
            }

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
    private class CustomSpinner extends JSpinner {
        public CustomSpinner(SpinnerNumberModel model) {
            super(model);
            customize();
        }

        private void customize() {
            setFont(new Font("メイリオ", Font.BOLD, 24));
            setBorder(null);
            setOpaque(false);

            // エディタのカスタマイズ
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor)getEditor();
            JTextField textField = editor.getTextField();
            textField.setBorder(null);
            textField.setFont(getFont());
            textField.setHorizontalAlignment(JTextField.RIGHT);
            textField.setOpaque(false);

            // 矢印ボタンのカスタマイズ
            replaceArrowButtons();

            // エディタパネルを透過に
            JComponent editorPane = (JComponent)getEditor();
            editorPane.setOpaque(false);
        }

        private void replaceArrowButtons() {
            for (Component c : getComponents()) {
                if (c instanceof JButton) {
                    JButton button = (JButton) c;
                    button.setBorderPainted(false);
                    button.setContentAreaFilled(false);
                    button.setFocusPainted(false);
                    button.setBackground(new Color(0, 0, 0, 0)); // 完全透過
                    button.setOpaque(false); // ボタンを透過に

                    // シンプルな矢印を描画
                    button.setUI(new BasicButtonUI() {
                        @Override
                        public void paint(Graphics g, JComponent c) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                            int w = c.getWidth();
                            int h = c.getHeight();

                            g2.setColor(new Color(100, 100, 100));

                            // ボタンの種類に応じて矢印を描画
                            if (button.getName().contains("Up")) {
                                int[] xPoints = {w/4, w/2, 3*w/4};
                                int[] yPoints = {2*h/3, h/3, 2*h/3};
                                g2.fillPolygon(xPoints, yPoints, 3);
                            } else {
                                int[] xPoints = {w/4, w/2, 3*w/4};
                                int[] yPoints = {h/3, 2*h/3, h/3};
                                g2.fillPolygon(xPoints, yPoints, 3);
                            }
                            g2.dispose();
                        }
                    });
                }
            }
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
        RoundedPanel logoPanel = new RoundedPanel(
                new BorderLayout(),
                Color.WHITE,
                CORNER_RADIUS,
                true,
                new Color(170, 170, 180),
                new Color(90, 90, 110)
        );
        logoPanel.setBorder(BorderFactory.createEmptyBorder(25, 10, 5, 10));  // 上下のpaddingを15に調整

        JPanel centeringPanel = new JPanel(new GridBagLayout());  // GridBagLayoutで中央寄せ
        centeringPanel.setOpaque(false);

        JLabel logoLabel = new JLabel("Smart Pill Dispenser");
        logoLabel.setFont(new Font("Yu Gothic", Font.BOLD, 36));
        logoLabel.setForeground(new Color(240, 240, 250));

        centeringPanel.add(logoLabel);
        logoPanel.add(centeringPanel, BorderLayout.CENTER);

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
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        mainPanel.add(logoPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(commandPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setSize(1400, 850);
        setLocationRelativeTo(null);

        // init label
        for (int i = 1; i <= 3; i++) {
            updateStockAmount(i, controller.model.stockManager.getStock(i));
        }
    }

    // for demo
    public void demoHistory() {
        for (int i = 18; i <= 24; i++) {
            LocalDate date = LocalDate.of(2024, 11, i);
            LocalTime time = LocalTime.of(9, 30, 45);
            int[] amounts = {1, 1, 0}; // 薬1: 2個, 薬2: 1個, 薬3: 3個
            this.addMedicationHistory(date, time, amounts);
        }
        LocalDate date = LocalDate.of(2024, 11, 22);
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

    // FormMainクラスに新しいイベントハンドラメソッドを追加
    public void onStockAmountClicked(int pillNumber) {
        String input = JOptionPane.showInputDialog(
                this,
                "追加する個数を入力してください",
                "在庫追加",
                JOptionPane.PLAIN_MESSAGE
        );

        if (input != null) {
            try {
                int addAmount = Integer.parseInt(input);
                if (addAmount > 0) {
                    controller.model.stockManager.addStock(pillNumber, addAmount, controller.view);
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "入力値が不正です。",
                            "エラー",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                System.out.println("ERROR : Invalid input");
                JOptionPane.showMessageDialog(
                        this,
                        "入力値が不正です。",
                        "エラー",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
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
                            "くすり1: " + medicationAmounts[0] + "個",
                            "くすり2: " + medicationAmounts[1] + "個",
                            "くすり3: " + medicationAmounts[2] + "個");

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
    public void updateStockAmount(int pillNumber, int newAmount) {
        // UI更新
        JLabel stockLabel = stockLabels.get(pillNumber);
        if (stockLabel != null) {
            stockLabel.setText(String.valueOf(newAmount));
        }
    }

    private JScrollPane createCalendarPanel() {
        LocalDate startDate = LocalDate.of(2024, 11, 17);
        LocalDate endDate = LocalDate.of(2024, 11, 30);
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

        int rows = (int) Math.ceil(daysBetween / 7.0);
        JPanel panel = new JPanel(new GridLayout(rows, 7, 5, 5));
        panel.setBackground(new Color(240, 240, 240));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");

        JScrollPane mainScrollPane = new JScrollPane(panel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setSize(new Dimension(0, 0));

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

        // 日付と曜日を含むパネル
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        headerPanel.setOpaque(false);

        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(titleFont);

        LocalDate localDate = LocalDate.of(2024, 11, Integer.parseInt(date.split("/")[1]));
        String dayOfWeek = switch (localDate.getDayOfWeek()) {
            case MONDAY -> "（月）";
            case TUESDAY -> "（火）";
            case WEDNESDAY -> "（水）";
            case THURSDAY -> "（木）";
            case FRIDAY -> "（金）";
            case SATURDAY -> "（土）";
            case SUNDAY -> "（日）";
        };

        JLabel dowLabel = new JLabel(dayOfWeek);
        dowLabel.setFont(titleFont);

        headerPanel.add(dateLabel);
        headerPanel.add(dowLabel);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel timeSlots = new JPanel();
        timeSlots.setLayout(new BoxLayout(timeSlots, BoxLayout.Y_AXIS));
        timeSlots.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        timeSlots.setOpaque(false);

        timeSlots.add(Box.createVerticalGlue());

        panel.add(timeSlots, BorderLayout.CENTER);

        return panel;
    }

    // createInventoryPanel() 内で、個別のpillPanel作成の代わりに使用
    private JPanel createPillStockPanel(String icon, Color iconColor, String amount, int pillNumber) {
        JPanel pillPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0) {
            @Override
            public void layoutContainer(Container target) {
                super.layoutContainer(target);
                int maxHeight = 0;
                for (Component comp : target.getComponents()) {
                    maxHeight = Math.max(maxHeight, comp.getPreferredSize().height);
                }

                for (Component comp : target.getComponents()) {
                    int y = maxHeight - comp.getPreferredSize().height;
                    // アイコンと単位ラベルの場合は少し上にオフセット
                    if (comp instanceof JLabel) {
                        JLabel label = (JLabel) comp;
                        if (label.getText().equals("個")) {
                            y -= 4;  // 3ピクセル上に調整
                        } else if (label.getText().matches("^[🍎🐟🌷]$")) {
                            y -= 5;
                        }
                    }
                    comp.setLocation(comp.getX(), y);
                }
            }
        });
        pillPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        iconLabel.setForeground(iconColor);

        // 在庫数ラベルをクリック可能に
        JLabel stockLabel = new JLabel(amount);
        stockLabel.setFont(new Font("メイリオ", Font.BOLD, 20));
        stockLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // マウスカーソルを手の形に

        // Maps に保存
        stockLabels.put(pillNumber, stockLabel);

        stockLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onStockAmountClicked(pillNumber);
            }

            // ホバー効果を追加（オプション）
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                stockLabel.setForeground(iconColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                stockLabel.setForeground(Color.BLACK);
            }
        });

        JLabel unitLabel = new JLabel("個");
        unitLabel.setFont(new Font("メイリオ", Font.BOLD, 13));

        pillPanel.add(iconLabel);
        pillPanel.add(Box.createHorizontalStrut(4));
        pillPanel.add(stockLabel);
        pillPanel.add(Box.createHorizontalStrut(2));
        pillPanel.add(unitLabel);

        return pillPanel;
    }

    private JPanel createInventoryPanel() {
        Color panelColor = getTimingColor("在庫");
        Color panelColor2 = getTimingColor2("在庫");

        RoundedPanel outerPanel = new RoundedPanel(
                new BorderLayout(),
                panelColor,
                CORNER_RADIUS,
                true, // グラデーションを使用
                panelColor2, // グラデーション開始色
                panelColor    // グラデーション終了色
        );
        outerPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

        JLabel titleLabel = new JLabel("在庫");
        titleLabel.setFont(new Font("メイリオ", Font.BOLD, (int)(20)));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 7, 7, 0));

        outerPanel.add(titleLabel, BorderLayout.NORTH);

        Color panelColorInner = getTimingColorInner("在庫");
        Color panelColorInner2 = getTimingColorInner2("在庫");

        RoundedPanel innerPanel = new RoundedPanel(
                new BorderLayout(),
                panelColor2,
                CORNER_RADIUS,
                true, // グラデーションを使用
                Color.WHITE, // グラデーション開始色
                Color.WHITE    // グラデーション終了色
        );
        innerPanel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));

        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        contentPanel.add(createPillStockPanel("🍎", new Color(224, 63, 63), "20", 1));
        contentPanel.add(Box.createHorizontalStrut(14));
        contentPanel.add(createPillStockPanel("🐟", new Color(45, 114, 226), "15", 2));
        contentPanel.add(Box.createHorizontalStrut(14));
        contentPanel.add(createPillStockPanel("🌷", new Color(232, 79, 166), "10", 3));

        innerPanel.add(contentPanel, BorderLayout.CENTER);
        outerPanel.add(innerPanel, BorderLayout.CENTER);

        return outerPanel;
    }

    private JPanel createTimeSlotPanel(String time, String... medications) {
        RoundedPanel borderedPanel = new RoundedPanel(
                new BorderLayout(),
                Color.WHITE, // デフォルトの背景色（グラデーションの場合は使用されません）
                CORNER_RADIUS,
                true, // グラデーションを使用
                new Color(232, 232, 240), // グラデーション開始色
                new Color(228, 228, 234)    // グラデーション終了色
        );
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
        containerPanel.setPreferredSize(new Dimension(303, 0));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));
        containerPanel.setOpaque(false);

        // 在庫管理パネルを追加
        JPanel inventoryPanel = createInventoryPanel();
        containerPanel.add(inventoryPanel, BorderLayout.NORTH);

        // Create panel for timing settings
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setOpaque(false);

        // Add timing settings
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        for (TimingType timing : TimingType.values()) {
            int p1 = controller.model.pillSets.getPillSet(timing.pillSetName).getCount(1);
            int p2 = controller.model.pillSets.getPillSet(timing.pillSetName).getCount(2);
            int p3 = controller.model.pillSets.getPillSet(timing.pillSetName).getCount(3);

            JPanel panel = createTimeSetting(timing.getLabel(),
                    new int[]{p1, p2, p3}, timing.defaultHour, timing.defaultMinute);
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
            settingsPanel.add(panel);
            settingsPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        }

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(settingsPanel);

        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        // Adjust scroll speed
        adjustScrollSpeed(scrollPane.getVerticalScrollBar(), 16, 50);

        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }
    private JPanel createMedicationRow(int index, String icon, Color color, int amount, TimingType timing) {
        JPanel row = new JPanel();
        row.setLayout(new FlowLayout(FlowLayout.CENTER, 0, -2));
        row.setOpaque(false);

        // アイコンとくすりラベルのコンテナ
        JPanel iconLabelContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)) ;
        iconLabelContainer.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        iconLabel.setForeground(color);
        iconLabelContainer.add(iconLabel);

        JLabel label = new JLabel("くすり" + (index + 1));
        label.setFont(baseFont);
        iconLabelContainer.add(label);

        row.add(iconLabelContainer);
        row.add(Box.createHorizontalStrut(15));

        // スピナーと単位のコンテナ
        JPanel spinnerUnitContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        spinnerUnitContainer.setOpaque(false);

        JSpinner spinner = new CustomSpinner(new SpinnerNumberModel(amount, 0, 9, 1));
        spinner.setPreferredSize(new Dimension(40, spinner.getPreferredSize().height));

        JSpinner.NumberEditor editor = (JSpinner.NumberEditor)spinner.getEditor();
        editor.getTextField().setForeground(color);
        editor.getTextField().setFont(new Font("メイリオ", Font.BOLD, 24));

        final int pillNumber = index + 1;
        spinner.addChangeListener(e -> {
            int newAmount = (Integer) ((JSpinner)e.getSource()).getValue();
            onMedicationAmountChanged(timing, pillNumber, newAmount);
        });
        spinnerUnitContainer.add(spinner);

        JLabel unitLabel = new JLabel("個");
        unitLabel.setFont(baseFont);
        spinnerUnitContainer.add(unitLabel);

        row.add(spinnerUnitContainer);

        return row;
    }

    private JPanel createTimeSetting(String time, int[] amounts, int hour, int minute) {
        Color panelColor = getTimingColor(time);
        Color panelColor2 = getTimingColor2(time);

        RoundedPanel outerPanel = new RoundedPanel(new BorderLayout(), panelColor, CORNER_RADIUS);
        outerPanel = new RoundedPanel(
                new BorderLayout(),
                panelColor2,
                CORNER_RADIUS,
                true, // グラデーションを使用
                panelColor2, // グラデーション開始色
                panelColor    // グラデーション終了色
        );
        outerPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

        TimingType timing = Arrays.stream(TimingType.values())
                .filter(t -> t.getLabel().equals(time))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid timing: " + time));

        JLabel titleLabel = new JLabel(time);
        titleLabel.setFont(new Font("メイリオ", Font.BOLD, (int)(20)));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 7, 7, 0));

        outerPanel.add(titleLabel, BorderLayout.NORTH);

        Color panelColorInner = getTimingColorInner(time);
        Color panelColorInner2 = getTimingColorInner2(time);

        RoundedPanel innerPanel = new RoundedPanel(
                new BorderLayout(),
                panelColor2,
                CORNER_RADIUS,
                true, // グラデーションを使用
                panelColorInner2, // グラデーション開始色
                panelColorInner    // グラデーション終了色
        );
        innerPanel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));

        // メディケーションパネルを BoxLayout に変更
        JPanel medicationPanel = new JPanel();
        medicationPanel.setLayout(new BoxLayout(medicationPanel, BoxLayout.Y_AXIS));
        medicationPanel.setOpaque(false);
        medicationPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        String[] icons = {"🍎", "🐟", "🌷"};
        Color[] colors = {
                new Color(224, 63, 63),
                new Color(45, 114, 226),
                new Color(232, 79, 166)
        };

        // くすりの行と区切り線を追加
        for (int i = 0; i < 3; i++) {
            medicationPanel.add(createMedicationRow(i, icons[i], colors[i], amounts[i], timing));

            if (i < 2) {
                JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
                separator.setForeground(new Color(230, 230, 230));
                medicationPanel.add(Box.createRigidArea(new Dimension(0, 2)));  // 区切り線の上に少し空間
                medicationPanel.add(separator);
                medicationPanel.add(Box.createRigidArea(new Dimension(0, 2)));  // 区切り線の下に少し空間
            }
        }

        // Time settings panel
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setOpaque(false);

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timePanel.setOpaque(false);
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        timePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, timePanel.getPreferredSize().height));

        JCheckBox dailyCheck = new JCheckBox("毎日");
        dailyCheck.setFont(baseFont);
        dailyCheck.setOpaque(false);

        JSpinner hourSpinner = new CustomSpinner(new SpinnerNumberModel(hour, 0, 23, 1));
        hourSpinner.setPreferredSize(new Dimension(55, hourSpinner.getPreferredSize().height));

        JSpinner minuteSpinner = new CustomSpinner(new SpinnerNumberModel(minute, 0, 59, 1));
        minuteSpinner.setPreferredSize(new Dimension(55, minuteSpinner.getPreferredSize().height));

        JLabel hourLabel = new JLabel("時");
        hourLabel.setFont(new Font("メイリオ", Font.BOLD, 20));

        JLabel minuteLabel = new JLabel("分");
        minuteLabel.setFont(new Font("メイリオ", Font.BOLD, 20));

        JPanel medicationLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 9, 0));
        medicationLabelPanel.setOpaque(false);
        medicationLabelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel medicationLabel = new JLabel("に服用");
        medicationLabel.setFont(baseFont);
        medicationLabelPanel.add(medicationLabel);

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

        JPanel dispensePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dispensePanel.setOpaque(false);
        dispensePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton dispenseButton = new RoundedButton("手動排出");
        dispenseButton.setFont(baseFont);
        dispenseButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        dispenseButton.addActionListener(e -> onDispenseButtonClicked(timing));
        dispensePanel.add(dispenseButton);

        southPanel.add(timePanel);
        southPanel.add(medicationLabelPanel);
        southPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        southPanel.add(dispensePanel);

        innerPanel.add(medicationPanel, BorderLayout.CENTER);
        innerPanel.add(southPanel, BorderLayout.SOUTH);

        outerPanel.add(innerPanel, BorderLayout.CENTER);

        return outerPanel;
    }

    private Color getTimingColor(String time) {
        switch (time) {
            case "朝": return new Color(240, 85, 85);  // Morning red
            case "昼": return new Color(227, 183, 80); // Noon orange
            case "夜": return new Color(85, 62, 189);  // Night blue
            default: return Color.GRAY;
        }
    }
    private Color getTimingColor2(String time) {
        switch (time) {
            case "朝": return new Color(252, 120, 120);  // Morning red
            case "昼": return new Color(247, 213, 140); // Noon orange
            case "夜": return new Color(123, 99, 237);  // Night blue
            default: return Color.GRAY;
        }
    }

    private Color getTimingColorInner(String time) {
        switch (time) {
            case "朝": return new Color(255, 250, 250);  // Morning red
            case "昼": return new Color(255, 255, 235); // Noon orange
            case "夜": return new Color(245, 245, 255);  // Night blue
            default: return Color.GRAY;
        }
    }
    private Color getTimingColorInner2(String time) {
        switch (time) {
            case "朝": return new Color(255, 255, 255);  // Morning red
            case "昼": return new Color(255, 255, 255); // Noon orange
            case "夜": return new Color(255, 255, 255);  // Night blue
            default: return Color.GRAY;
        }
    }

}