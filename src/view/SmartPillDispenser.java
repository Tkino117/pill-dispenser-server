package view;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SmartPillDispenser extends JFrame {
    // クラスの先頭に列挙型を追加
    public enum TimingType {
        MORNING("朝"),
        NOON("昼"),
        NIGHT("夜");

        private final String label;

        TimingType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    // 基本フォントサイズを1.5倍に設定
    private final Font baseFont = new Font(Font.DIALOG, Font.BOLD, (int)(12 * 1.5));
    private final Font titleFont = new Font(Font.DIALOG, Font.BOLD, (int)(14 * 1.5));

    // 角丸の半径を定義
    private static final int CORNER_RADIUS = 15;

    // 日付パネルを保持するためのマップを追加
    private final Map<LocalDate, JPanel> datePanelsMap = new HashMap<>();

    // 角丸パネルの内部クラス
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

    // カスタム角丸ボタンクラス
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


    public SmartPillDispenser() {
        setTitle("Smart Pill Dispenser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setUIFont(new javax.swing.plaf.FontUIResource(baseFont));

        // ロゴパネルを作成
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(240, 240, 240));  // 背景色を設定
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));  // 上下に余白を設定

        // ロゴテキストを作成
        JLabel logoLabel = new JLabel("Smart Pill Dispenser");
        logoLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 36));  // フォントサイズを36ptに設定
        logoPanel.add(logoLabel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane calendarScrollPane = createCalendarPanel();
        JPanel settingsPanel = createSettingsPanel();

        mainPanel.add(logoPanel, BorderLayout.NORTH);  // ロゴパネルを追加
        mainPanel.add(calendarScrollPane, BorderLayout.CENTER);
        mainPanel.add(settingsPanel, BorderLayout.EAST);

        add(mainPanel);

        setSize(1600, 1000);
        setLocationRelativeTo(null);

        // !!test!!
        LocalDate date = LocalDate.of(2024, 11, 15);
        LocalTime time = LocalTime.of(9, 30, 45);
        int[] amounts = {2, 1, 3}; // 薬1: 2個, 薬2: 1個, 薬3: 3個
        this.addMedicationHistory(date, time, amounts);
        this.addMedicationHistory(date, time, amounts);
        this.addMedicationHistory(date, time, amounts);
        this.addMedicationHistory(date, time, amounts);
    }

    // 設定変更を処理するメソッドを追加
    public void onSettingsChanged(TimingType timing, boolean isDaily, int hour, int minute, int[] medicationAmounts) {
        // このメソッドをオーバーライドして使用
        System.out.println(String.format(
                "設定が変更されました - 時間帯: %s, 毎日: %b, %02d:%02d, 薬の個数: [%d, %d, %d]",
                timing.getLabel(), isDaily, hour, minute,
                medicationAmounts[0], medicationAmounts[1], medicationAmounts[2]
        ));
    }

    // UIマネージャーのデフォルトフォントを設定するヘルパーメソッド
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

    // スクロール速度を調整するヘルパーメソッド
    private void adjustScrollSpeed(JScrollBar scrollBar, int unitIncrement, int blockIncrement) {
        scrollBar.setUnitIncrement(unitIncrement);
        scrollBar.setBlockIncrement(blockIncrement);
    }

    // TimeSlotsコンテナを探すヘルパーメソッド
    private JPanel findTimeSlotsContainer(JPanel dayPanel) {
        Component centerComponent = ((BorderLayout)dayPanel.getLayout())
                .getLayoutComponent(dayPanel, BorderLayout.CENTER);

        if (centerComponent instanceof JPanel) {
            return (JPanel) centerComponent;
        }
        return null;
    }

    // 履歴追加のためのパブリックメソッド
    public void addMedicationHistory(LocalDate date, LocalTime time, int[] medicationAmounts) {
        if (medicationAmounts.length != 3) {
            throw new IllegalArgumentException("薬の数は3種類である必要があります");
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

                    timeSlotsContainer.add(timeSlot, 0);
                    timeSlotsContainer.add(Box.createRigidArea(new Dimension(0, 5)), 1);

                    timeSlotsContainer.revalidate();
                    timeSlotsContainer.repaint();
                }
            }
        });
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

        adjustScrollSpeed(mainScrollPane.getVerticalScrollBar(), 50, 200);
        adjustScrollSpeed(mainScrollPane.getHorizontalScrollBar(), 50, 200);

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

        // デフォルトの空白スペースを追加
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
        // GridLayoutからBoxLayoutに変更して上詰めを実現
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(330, 0));  // 300から330に変更
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        panel.setOpaque(false);

        // 朝、昼、夜のパネルを追加
        panel.add(createTimeSetting("朝", new int[]{2, 1, 3}));
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // パネル間のスペース
        panel.add(createTimeSetting("昼", new int[]{2, 2, 2}));
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // パネル間のスペース
        panel.add(createTimeSetting("夜", new int[]{2, 2, 2}));

        // 下部に可変スペースを追加して上詰めを実現
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createTimeSetting(String time, int[] amounts) {
        RoundedPanel panel = new RoundedPanel(new BorderLayout(), Color.WHITE, CORNER_RADIUS);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));

        // TimingTypeを取得
        TimingType timing = Arrays.stream(TimingType.values())
                .filter(t -> t.getLabel().equals(time))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid timing: " + time));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel(time);
        titleLabel.setFont(titleFont);
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // スピナーを保持する配列
        JSpinner[] spinners = new JSpinner[3];

        JPanel medicationPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        medicationPanel.setOpaque(false);

        for (int i = 0; i < 3; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.setOpaque(false);
            JLabel label = new JLabel("薬" + (i + 1) + ":");
            label.setFont(baseFont);
            row.add(label);

            spinners[i] = new JSpinner(new SpinnerNumberModel(amounts[i], 0, 10, 1));
            spinners[i].setFont(baseFont);
            Dimension spinnerSize = new Dimension(80, spinners[i].getPreferredSize().height);
            spinners[i].setPreferredSize(spinnerSize);

            JLabel unitLabel = new JLabel("個");
            unitLabel.setFont(baseFont);

            row.add(spinners[i]);
            row.add(unitLabel);
            medicationPanel.add(row);
        }

        panel.add(medicationPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setOpaque(false);

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.setOpaque(false);
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox dailyCheck = new JCheckBox("毎日");
        dailyCheck.setFont(baseFont);
        dailyCheck.setOpaque(false);

        JSpinner hourSpinner = new JSpinner(new SpinnerNumberModel(12, 0, 23, 1));
        hourSpinner.setFont(baseFont);
        JSpinner minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        minuteSpinner.setFont(baseFont);

        JLabel hourLabel = new JLabel("時");
        hourLabel.setFont(baseFont);
        JLabel minuteLabel = new JLabel("分に服用");
        minuteLabel.setFont(baseFont);

        // 設定変更リスナーを作成
        ChangeListener settingsChangeListener = e -> {
            int[] currentAmounts = new int[3];
            for (int i = 0; i < 3; i++) {
                currentAmounts[i] = (Integer) spinners[i].getValue();
            }

            int hour = (Integer) hourSpinner.getValue();
            int minute = (Integer) minuteSpinner.getValue();
            boolean isDaily = dailyCheck.isSelected();

            onSettingsChanged(timing, isDaily, hour, minute, currentAmounts);
        };

        // 各コンポーネントにリスナーを追加
        for (JSpinner spinner : spinners) {
            spinner.addChangeListener(settingsChangeListener);
        }
        hourSpinner.addChangeListener(settingsChangeListener);
        minuteSpinner.addChangeListener(settingsChangeListener);
        dailyCheck.addActionListener(e -> settingsChangeListener.stateChanged(null));

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
        dispenseButton.addActionListener(e -> {
            String currentTime = String.format("%02d:%02d:%02d",
                    java.time.LocalTime.now().getHour(),
                    java.time.LocalTime.now().getMinute(),
                    java.time.LocalTime.now().getSecond());
            JOptionPane.showMessageDialog(panel, time + "の薬を排出します\n排出時刻: " + currentTime);
        });
        dispensePanel.add(dispenseButton);

        southPanel.add(timePanel);
        southPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        southPanel.add(dispensePanel);

        panel.add(medicationPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SmartPillDispenser dispenser = new SmartPillDispenser();
            dispenser.setVisible(true);
        });
    }
}