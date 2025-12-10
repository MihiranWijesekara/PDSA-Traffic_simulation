package model;

public class Node {
    private final int index;
    private final String name;

    public Node(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
