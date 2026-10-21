
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Kahn {

    private final GraphMetrics metrics = new GraphMetrics();

    public List<Integer> topologicalSort(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException("Topological sort requires a directed graph");
        }

        metrics.reset();

        int n = graph.vertexCount();
        int[] indegree = new int[n];

        for (Edge edge : graph.getEdges()) {
            indegree[edge.to]++;
        }

        ArrayDeque<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> order = new ArrayList<>();

        while (!queue.isEmpty()) {
            int vertex = queue.remove();
            order.add(vertex);

            for (Edge edge : graph.neighbors(vertex)) {
                metrics.edgeScans++;

                indegree[edge.to]--;

                if (indegree[edge.to] == 0) {
                    queue.add(edge.to);
                }
            }
        }

        if (order.size() != n) {
            throw new IllegalStateException("Graph contains a cycle");
        }

        return order;
    }

    public GraphMetrics getMetrics() {
        return metrics;
    }
}
