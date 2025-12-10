package model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrafficNetwork {

    public static final int NODE_COUNT = 9; // A,B,C,D,E,F,G,H,T

    public static final int A = 0;
    public static final int B = 1;
    public static final int C = 2;
    public static final int D = 3;
    public static final int E = 4;
    public static final int F = 5;
    public static final int G = 6;
    public static final int H = 7;
    public static final int T = 8;

    private final List<Node> nodes = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    private final Random random = new Random();

    public TrafficNetwork() {
        nodes.add(new Node(A, "A"));
        nodes.add(new Node(B, "B"));
        nodes.add(new Node(C, "C"));
        nodes.add(new Node(D, "D"));
        nodes.add(new Node(E, "E"));
        nodes.add(new Node(F, "F"));
        nodes.add(new Node(G, "G"));
        nodes.add(new Node(H, "H"));
        nodes.add(new Node(T, "T"));

        // Roads (structure from assignment)
        edges.add(new Edge(A, B, 0));
        edges.add(new Edge(A, C, 0));
        edges.add(new Edge(A, D, 0));
        edges.add(new Edge(B, E, 0));
        edges.add(new Edge(B, F, 0));
        edges.add(new Edge(C, E, 0));
        edges.add(new Edge(C, F, 0));
        edges.add(new Edge(D, F, 0));
        edges.add(new Edge(E, G, 0));
        edges.add(new Edge(E, H, 0));
        edges.add(new Edge(F, H, 0));
        edges.add(new Edge(G, T, 0));
        edges.add(new Edge(H, T, 0));

        randomizeCapacities();
    }

    /** Each game round: random 5–15 capacity per road */
    public void randomizeCapacities() {
        for (Edge e : edges) {
            int cap = 5 + random.nextInt(11); // 5..15
            e.setCapacity(cap);
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    /** Build adjacency matrix capacity[u][v] */
    public int[][] buildCapacityMatrix() {
        int[][] capacity = new int[NODE_COUNT][NODE_COUNT];
        for (Edge e : edges) {
            capacity[e.getFrom()][e.getTo()] = e.getCapacity();
        }
        return capacity;
    }

    // Helpers used when saving to DB
    public int getCapAB() { return getCap(A, B); }
    public int getCapAC() { return getCap(A, C); }
    public int getCapAD() { return getCap(A, D); }
    public int getCapBE() { return getCap(B, E); }
    public int getCapBF() { return getCap(B, F); }
    public int getCapCE() { return getCap(C, E); }
    public int getCapCF() { return getCap(C, F); }
    public int getCapDF() { return getCap(D, F); }
    public int getCapEG() { return getCap(E, G); }
    public int getCapEH() { return getCap(E, H); }
    public int getCapFH() { return getCap(F, H); }
    public int getCapGT() { return getCap(G, T); }
    public int getCapHT() { return getCap(H, T); }

    private int getCap(int from, int to) {
        for (Edge e : edges) {
            if (e.getFrom() == from && e.getTo() == to) {
                return e.getCapacity();
            }
        }
        return 0;
    }
}
