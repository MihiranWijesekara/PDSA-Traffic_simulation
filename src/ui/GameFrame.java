package ui;

import algo.EdmondsKarpMaxFlow;
import algo.FordFulkersonMaxFlow;
import db.DbManager;
import model.GameRoundResult;
import model.TrafficNetwork;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private final DbManager dbManager;
    private final TrafficNetwork network;
    private final GraphPanel graphPanel;

    private final JTextField txtPlayerName = new JTextField(15);
    private final JTextField txtAnswer = new JTextField(8);
    private final JLabel lblStatus = new JLabel("Welcome! Click 'New Round' to start.");
    private final JLabel lblTimes = new JLabel(" ");
    private final JButton btnNewRound = new JButton("New Round");
    private final JButton btnSubmit = new JButton("Submit Answer");

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(230, 126, 34);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);

    public GameFrame(DbManager dbManager) {
        super("Traffic Simulation – Maximum Flow Game");
        this.dbManager = dbManager;
        this.network = new TrafficNetwork();
        this.graphPanel = new GraphPanel(network);

        buildUi();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void buildUi() {
        setLayout(new BorderLayout(8, 8));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(8, 8));
        headerPanel.setBackground(BACKGROUND_COLOR);

        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(CARD_COLOR);
        titlePanel.setBorder(createCardBorder());

        JLabel title = new JLabel("Traffic Simulation Problem – Max Flow");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(PRIMARY_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(10, 15, 3, 15));

        JLabel subtitle = new JLabel("Find maximum vehicle throughput from A to T");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        controlPanel.setBackground(CARD_COLOR);
        controlPanel.setBorder(createCardBorder());

        JLabel playerLabel = new JLabel("Player:");
        playerLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        playerLabel.setForeground(TEXT_PRIMARY);

        txtPlayerName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPlayerName.setBorder(createTextFieldBorder());
        txtPlayerName.setPreferredSize(new Dimension(140, 28));

        styleButton(btnNewRound, PRIMARY_COLOR);
        btnNewRound.setPreferredSize(new Dimension(110, 28));
        btnNewRound.addActionListener(e -> newRound());

        controlPanel.add(playerLabel);
        controlPanel.add(txtPlayerName);
        controlPanel.add(btnNewRound);

        headerPanel.add(titlePanel, BorderLayout.NORTH);
        headerPanel.add(controlPanel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(CARD_COLOR);
        centerPanel.setBorder(createCardBorder());

        JLabel graphTitle = new JLabel("Traffic Network (nodes A–T, capacities vehicles/min)");
        graphTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        graphTitle.setForeground(TEXT_PRIMARY);
        graphTitle.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        centerPanel.add(graphTitle, BorderLayout.NORTH);
        centerPanel.add(graphPanel, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 8));
        bottomPanel.setBackground(BACKGROUND_COLOR);

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        inputPanel.setBackground(CARD_COLOR);
        inputPanel.setBorder(createCardBorder());

        JLabel inputLabel = new JLabel("Your maximum flow guess (vehicles/min):");
        inputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputLabel.setForeground(TEXT_PRIMARY);

        txtAnswer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAnswer.setBorder(createTextFieldBorder());
        txtAnswer.setPreferredSize(new Dimension(70, 28));

        styleButton(btnSubmit, SUCCESS_COLOR);
        btnSubmit.setPreferredSize(new Dimension(120, 28));
        btnSubmit.addActionListener(e -> submitAnswer());

        inputPanel.add(inputLabel);
        inputPanel.add(txtAnswer);
        inputPanel.add(btnSubmit);

        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(CARD_COLOR);
        statusPanel.setBorder(createCardBorder());

        JPanel statusContent = new JPanel(new GridLayout(2, 1, 3, 3));
        statusContent.setBackground(CARD_COLOR);
        statusContent.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(PRIMARY_COLOR);

        lblTimes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTimes.setForeground(TEXT_SECONDARY);

        statusContent.add(lblStatus);
        statusContent.add(lblTimes);
        statusPanel.add(statusContent, BorderLayout.CENTER);

        bottomPanel.add(inputPanel, BorderLayout.NORTH);
        bottomPanel.add(statusPanel, BorderLayout.CENTER);

        return bottomPanel;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor, 0),
                BorderFactory.createEmptyBorder(6, 15, 6, 15)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 224, 230), 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
    }

    private Border createTextFieldBorder() {
        return BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
    }

    private static String fmtNanos(long ns) {
        if (ns < 1_000) return ns + " ns";
        if (ns < 1_000_000) return String.format("%.2f µs", ns / 1_000.0);
        if (ns < 1_000_000_000) return String.format("%.2f ms", ns / 1_000_000.0);
        return String.format("%.2f s", ns / 1_000_000_000.0);
    }

    private void newRound() {
        network.randomizeCapacities();
        txtAnswer.setText("");

        lblStatus.setText("New round started! Analyze the graph and enter your maximum flow estimate.");
        lblStatus.setForeground(PRIMARY_COLOR);

        lblTimes.setText(" "); // reset timing label
        graphPanel.repaint();
    }

    private void submitAnswer() {
        String input = txtAnswer.getText().trim();
        if (input.isEmpty()) {
            showStyledMessage("Please enter your maximum flow estimate.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int playerAnswer;
        try {
            playerAnswer = Integer.parseInt(input);
            if (playerAnswer < 0) throw new NumberFormatException("negative");
        } catch (NumberFormatException ex) {
            showStyledMessage("Please enter a valid non-negative integer.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int[][] capacity = network.buildCapacityMatrix();

        // ---- Algorithm 1: Ford-Fulkerson (DFS) ----
        long ffStart = System.nanoTime();
        int maxFlowFF = FordFulkersonMaxFlow.maxFlow(capacity, TrafficNetwork.A, TrafficNetwork.T);
        long ffNs = System.nanoTime() - ffStart;

        // ---- Algorithm 2: Edmonds-Karp (BFS) ----
        long ekStart = System.nanoTime();
        int maxFlowEK = EdmondsKarpMaxFlow.maxFlow(capacity, TrafficNetwork.A, TrafficNetwork.T);
        long ekNs = System.nanoTime() - ekStart;

        // Correct max flow for the game
        int correctMaxFlow = maxFlowFF;

        // Show execution times in UI
        if (maxFlowFF != maxFlowEK) {
            lblTimes.setText("WARNING: Algorithms disagree! FF=" + maxFlowFF + " EK=" + maxFlowEK +
                    " | FF time: " + fmtNanos(ffNs) + " | EK time: " + fmtNanos(ekNs));
        } else {
            lblTimes.setText("Ford-Fulkerson(DFS): " + fmtNanos(ffNs) +
                    " | Edmonds-Karp(BFS): " + fmtNanos(ekNs));
        }

        int diff = Math.abs(playerAnswer - correctMaxFlow);
        String result;
        Color resultColor;
        String prefix;

        if (playerAnswer == correctMaxFlow) {
            result = "WIN";
            resultColor = SUCCESS_COLOR;
            prefix = "PERFECT";
        } else if (diff <= 2) {
            result = "DRAW";
            resultColor = WARNING_COLOR;
            prefix = "CLOSE";
        } else {
            result = "LOSE";
            resultColor = DANGER_COLOR;
            prefix = "TRY AGAIN";
        }

        lblStatus.setText(String.format(
                "%s | Correct: %d vehicles/min | Your answer: %d | Difference: %d",
                prefix, correctMaxFlow, playerAnswer, diff
        ));
        lblStatus.setForeground(resultColor);

        // ✅ Save player ONLY if WIN (correct answer)
        String name = txtPlayerName.getText().trim();
        int playerId = -1;
        if ("WIN".equals(result) && !name.isBlank()) {
            playerId = dbManager.getOrCreatePlayerId(name);
        }

        // ✅ Save game round ALWAYS (playerId = -1 will be stored as NULL in DB if you updated DbManager)
        GameRoundResult gr = new GameRoundResult(playerId, correctMaxFlow, playerAnswer, result, network);
        int roundId = dbManager.saveGameRound(gr);

        // ✅ Save algorithm execution time for EACH round
        dbManager.saveAlgorithmRun(roundId, "Ford-Fulkerson(DFS)", ffNs / 1_000_000.0);
        dbManager.saveAlgorithmRun(roundId, "Edmonds-Karp(BFS)", ekNs / 1_000_000.0);

        int option = JOptionPane.showOptionDialog(
                this,
                "Result: " + result +
                        "\nYour guess was " + (playerAnswer == correctMaxFlow ? "correct!" : "incorrect!") +
                        "\n\nExecution Times:\n" +
                        "Ford-Fulkerson(DFS): " + fmtNanos(ffNs) + "\n" +
                        "Edmonds-Karp(BFS): " + fmtNanos(ekNs),
                "Game Result",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"OK"},
                null
        );

        if (option == JOptionPane.OK_OPTION) {
            newRound();
        }
    }


    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", CARD_COLOR);
        UIManager.put("Panel.background", CARD_COLOR);
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}
