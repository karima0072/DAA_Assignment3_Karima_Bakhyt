
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DagShortestPath {

    private final GraphMetrics metrics = new GraphMetrics();

    public static class Result {
        private final long[] dist;
        private final int[] parent;

        public Result(long[] dist, int[] parent) {
            this.dist = dist.clone();
            this.parent = parent.clone();
        }

        public long getDistance(int vertex) {
            return dist[vertex];
        }

        public long sumDistances() {
            long sum = 0;

            for (long d : dist) {
                sum += d;
            }

            return sum;
        }

        public List<Integer> getPath(int vertex) {
            List<Integer> path = new ArrayList<>();
            int current = vertex;

            while (current != -1) {
                path.add(current);
                current = parent[current];
            }

            Collections.reverse(path);
            return path;
        }
    }

    public Result shortestPaths(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException("DAG must be directed");
        }

        metrics.reset();

        Kahn kahn = new Kahn();
        List<Integer> order = kahn.topologicalSort(graph);

        int n = graph.vertexCount();
        int[] indegree = new int[n];

        for (Edge edge : graph.getEdges()) {
            indegree[edge.to]++;
        }

        long[] dist = new long[n];
        int[] parent = new int[n];

        long inf = Long.MAX_VALUE / 4;

        for (int i = 0; i < n; i++) {
            dist[i] = indegree[i] == 0 ? 0 : inf;
            parent[i] = -1;
        }

        for (int vertex : order) {
            if (dist[vertex] == inf) {
                continue;
            }

            for (Edge edge : graph.neighbors(vertex)) {
                metrics.edgeScans++;

                long candidate = dist[vertex] + edge.weight;

                if (candidate < dist[edge.to]) {
                    dist[edge.to] = candidate;
                    parent[edge.to] = vertex;
                }
            }
        }

        return new Result(dist, parent);
    }

    public GraphMetrics getMetrics() {
        return metrics;
    }
}
