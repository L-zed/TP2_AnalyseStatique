package graph;

public class Edge {
    private  String node1;
    private  String node2;
    private int weight;

    public Edge(String node1, String node2) {
        this.node1 = node1;
        this.node2 = node2;
        this.weight = 1;
    }

    public int getWeight() {
        return weight;
    }

    public String getNode1() {
        return node1;
    }

    public String getNode2() {
        return node2;
    }

    public void incrementWeight() {
        this.weight++;
    }

}
