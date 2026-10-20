import java.util.ArrayList;
import java.util.List;

public class Graph {

    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adjacencyList;
    private final List<Edge> edges;

    public Graph(int n, boolean directed) {
        if (n < 0) {
            throw new IllegalArgumentException("Negative vertex count");
        }

        this.n = n;
        this.directed = directed;
        this.adjacencyList = new ArrayList<>();
        this.edges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, int weight) {
        checkVertex(from);
        checkVertex(to);

        Edge edge = new Edge(from, to, weight);

        adjacencyList.get(from).add(edge);
        edges.add(edge);

        if (!directed) {
            adjacencyList.get(to).add(
                    new Edge(to, from, weight)
            );
        }
    }

    public List<Edge> neighbors(int vertex) {
        checkVertex(vertex);
        return adjacencyList.get(vertex);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int vertexCount() {
        return n;
    }

    public int edgeCount() {
        return edges.size();
    }

    public boolean isDirected() {
        return directed;
    }

    private void checkVertex(int vertex) {
        if (vertex < 0 || vertex >= n) {
            throw new IndexOutOfBoundsException(
                    "Invalid vertex: " + vertex
            );
        }
    }
}