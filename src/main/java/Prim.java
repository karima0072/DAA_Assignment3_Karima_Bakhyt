
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {

    private long edgeScans;
    private long comparisons;

    public MSTResult findMST(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException("MST requires an undirected graph");
        }

        edgeScans = 0;
        comparisons = 0;

        int n = graph.vertexCount();
        boolean[] visited = new boolean[n];

        PriorityQueue<Edge> queue = new PriorityQueue<>(
                (a, b) -> {
                    comparisons++;
                    return Integer.compare(a.weight, b.weight);
                }
        );

        List<Edge> selectedEdges = new ArrayList<>();
        long totalWeight = 0;
        int components = 0;

        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            components++;
            queue.add(new Edge(-1, start, 0));

            while (!queue.isEmpty()) {
                Edge edge = queue.poll();
                int vertex = edge.to;

                if (visited[vertex]) {
                    continue;
                }

                visited[vertex] = true;

                if (edge.from != -1) {
                    selectedEdges.add(edge);
                    totalWeight += edge.weight;
                }

                for (Edge next : graph.neighbors(vertex)) {
                    edgeScans++;

                    if (!visited[next.to]) {
                        queue.add(next);
                    }
                }
            }
        }

        return new MSTResult(selectedEdges, totalWeight, components);
    }

    public long getEdgeScans() {
        return edgeScans;
    }

    public long getComparisons() {
        return comparisons;
    }
}
