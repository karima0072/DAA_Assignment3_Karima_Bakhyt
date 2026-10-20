
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal {

    private long edgeScans;
    private long comparisons;
    private long dsuOps;

    public MSTResult findMST(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException("MST requires an undirected graph");
        }

        edgeScans = 0;
        comparisons = 0;
        dsuOps = 0;

        int n = graph.vertexCount();
        DSU dsu = new DSU(n);

        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());

        sortedEdges.sort((a, b) -> {
            comparisons++;
            return Integer.compare(a.weight, b.weight);
        });

        List<Edge> selectedEdges = new ArrayList<>();
        long totalWeight = 0;

        for (Edge edge : sortedEdges) {
            edgeScans++;

            if (dsu.union(edge.from, edge.to)) {
                selectedEdges.add(edge);
                totalWeight += edge.weight;
            }

            if (dsu.components() == 1) {
                break;
            }
        }

        dsuOps = dsu.getFindCalls() + dsu.getSuccessfulUnions();

        return new MSTResult(
                selectedEdges,
                totalWeight,
                dsu.components()
        );
    }

    public long getEdgeScans() {
        return edgeScans;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getDsuOps() {
        return dsuOps;
    }
}
