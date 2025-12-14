package ui;

import model.Edge;
import model.Node;
import model.TrafficNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

public class GraphPanel extends JPanel {

    private final TrafficNetwork network;
    private final Map<Integer, Point> positions = new HashMap<>();

    // Modern color scheme
    private static final Color NODE_COLOR = new Color(52, 152, 219);
    private static final Color NODE_BORDER = new Color(41, 128, 185);
    private static final Color SOURCE_COLOR = new Color(46, 204, 113);
    private static final Color SINK_COLOR = new Color(231, 76, 60);
    private static final Color EDGE_COLOR = new Color(149, 165, 166);
    private static final Color CAPACITY_BG = new Color(236, 240, 241);
    private static final Color CAPACITY_TEXT = new Color(52, 73, 94);
    private static final Color NODE_TEXT = Color.WHITE;

    public GraphPanel(TrafficNetwork network) {
        this.network = network;
        setPreferredSize(new Dimension(700, 450));
        setBackground(Color.WHITE);
        initPositions();
    }

    private void initPositions() {
        positions.put(TrafficNetwork.A, new Point(80, 225));
        positions.put(TrafficNetwork.B, new Point(200, 100));
        positions.put(TrafficNetwork.C, new Point(200, 320));
        positions.put(TrafficNetwork.D, new Point(200, 210));
        positions.put(TrafficNetwork.E, new Point(360, 100));
        positions.put(TrafficNetwork.F, new Point(360, 280));
        positions.put(TrafficNetwork.G, new Point(520, 90));
        positions.put(TrafficNetwork.H, new Point(520, 290));
        positions.put(TrafficNetwork.T, new Point(640, 200));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Draw edges first (so nodes appear on top)
        for (Edge e : network.getEdges()) {
            Point from = positions.get(e.getFrom());
            Point to = positions.get(e.getTo());
            drawStyledArrow(g2, from, to, e.getCapacity());
        }

        // Draw nodes
        for (Node n : network.getNodes()) {
            Point p = positions.get(n.getIndex());
            Color nodeColor = getNodeColor(n.getIndex());
            drawStyledNode(g2, p.x, p.y, n.getName(), nodeColor);
        }
    }

    private Color getNodeColor(int nodeIndex) {
        if (nodeIndex == TrafficNetwork.A) {
            return SOURCE_COLOR;
        } else if (nodeIndex == TrafficNetwork.T) {
            return SINK_COLOR;
        } else {
            return NODE_COLOR;
        }
    }

    private void drawStyledNode(Graphics2D g2, int x, int y, String label, Color color) {
        int radius = 32;

        // Draw shadow
        g2.setColor(new Color(0, 0, 0, 25));
        g2.fillOval(x - radius/2 + 2, y - radius/2 + 2, radius, radius);

        // Draw node background
        g2.setColor(color);
        g2.fill(new Ellipse2D.Double(x - radius/2, y - radius/2, radius, radius));

        // Draw border
        g2.setColor(color.darker());
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(new Ellipse2D.Double(x - radius/2, y - radius/2, radius, radius));

        // Draw label
        g2.setColor(NODE_TEXT);
        Font nodeFont = new Font("Segoe UI", Font.BOLD, 16);
        g2.setFont(nodeFont);
        FontMetrics fm = g2.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        int labelHeight = fm.getAscent();
        g2.drawString(label, x - labelWidth/2, y + labelHeight/2 - 2);
    }

    private void drawStyledArrow(Graphics2D g2, Point from, Point to, int capacity) {
        // Calculate direction
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double angle = Math.atan2(dy, dx);
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Shorten line to account for node radius
        int nodeRadius = 16;
        int startX = from.x + (int)(nodeRadius * Math.cos(angle));
        int startY = from.y + (int)(nodeRadius * Math.sin(angle));
        int endX = to.x - (int)(nodeRadius * Math.cos(angle));
        int endY = to.y - (int)(nodeRadius * Math.sin(angle));

        // Draw edge line
        g2.setColor(EDGE_COLOR);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(startX, startY, endX, endY);

        // Draw arrowhead
        drawArrowHead(g2, endX, endY, angle);

        // Draw capacity label
        drawCapacityLabel(g2, from, to, capacity);
    }

    private void drawArrowHead(Graphics2D g2, int x, int y, double angle) {
        int arrowSize = 10;
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];

        xPoints[0] = x;
        yPoints[0] = y;
        xPoints[1] = x - (int)(arrowSize * Math.cos(angle - Math.PI / 6));
        yPoints[1] = y - (int)(arrowSize * Math.sin(angle - Math.PI / 6));
        xPoints[2] = x - (int)(arrowSize * Math.cos(angle + Math.PI / 6));
        yPoints[2] = y - (int)(arrowSize * Math.sin(angle + Math.PI / 6));

        g2.setColor(EDGE_COLOR.darker());
        g2.fillPolygon(xPoints, yPoints, 3);
    }

    private void drawCapacityLabel(Graphics2D g2, Point from, Point to, int capacity) {
        int midX = (from.x + to.x) / 2;
        int midY = (from.y + to.y) / 2;

        // Offset label slightly for better visibility
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double perpX = -dy / Math.sqrt(dx*dx + dy*dy);
        double perpY = dx / Math.sqrt(dx*dx + dy*dy);

        int offsetX = midX + (int)(perpX * 12);
        int offsetY = midY + (int)(perpY * 12);

        String label = String.valueOf(capacity);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        g2.setFont(labelFont);
        FontMetrics fm = g2.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        int labelHeight = fm.getHeight();

        // Draw background rectangle
        int padding = 6;
        int bgX = offsetX - labelWidth/2 - padding;
        int bgY = offsetY - labelHeight/2 - padding + 2;
        int bgWidth = labelWidth + padding * 2;
        int bgHeight = labelHeight + padding;

        g2.setColor(CAPACITY_BG);
        g2.fillRoundRect(bgX, bgY, bgWidth, bgHeight, 8, 8);

        g2.setColor(new Color(189, 195, 199));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(bgX, bgY, bgWidth, bgHeight, 8, 8);

        // Draw capacity text
        g2.setColor(CAPACITY_TEXT);
        g2.drawString(label, offsetX - labelWidth/2, offsetY + fm.getAscent()/2 - 2);
    }
}
