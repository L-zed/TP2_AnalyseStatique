package graph;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Graph {
    private List<String> nodes;
    private List<Edge> edges;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public List<String> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addNode(String node){
        if ( !nodes.contains(node) ){
            nodes.add(node);
        }
    }


    public Edge findEdge (String node1, String node2) {
        Optional<Edge> result =
                edges.stream()
                        .filter(e -> e.getNode1().equals(node1) && e.getNode2().equals(node2) ||
                                e.getNode1().equals(node2) && e.getNode2().equals(node1))
                        .findFirst();
        return result.orElse(null);
    }

    public void addEdge(String node1, String node2) {
        Optional<Edge> result =
                edges.stream()
                        .filter(e -> e.getNode1().equals(node1) && e.getNode2().equals(node2) ||
                                e.getNode1().equals(node2) && e.getNode2().equals(node1))
                        .findFirst();
        if (!result.isPresent()) {
            this.edges.add(new Edge(node1, node2));
        }
        else {
            result.get().incrementWeight();
        }
    }

    public float couplingClasses(String firstClass, String secondClass) {
        float r = 0;
        if (findEdge(firstClass,secondClass) != null){
            Edge edge = findEdge(firstClass,secondClass);
            int total = 0;
            for (Edge e : this.getEdges() ){
                total += e.getWeight();
            }
            r = (float) edge.getWeight() / (float) total;
        }
        return r;
    }
}
