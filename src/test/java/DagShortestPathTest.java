
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DagShortestPathTest {

    @Test
    void testSimpleDAG() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 3);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 3, 1);

        DagShortestPath algorithm = new DagShortestPath();
        DagShortestPath.Result result = algorithm.shortestPaths(graph);

        assertEquals(0, result.getDistance(0));
        assertEquals(3, result.getDistance(1));
        assertEquals(10, result.getDistance(2));
        assertEquals(5, result.getDistance(3));

        assertEquals(Arrays.asList(0, 1, 3), result.getPath(3));
        assertEquals(graph.edgeCount(), algorithm.getMetrics().edgeScans);
    }

    @Test
    void testNegativeWeights() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, -10);
        graph.addEdge(2, 3, 1);

        DagShortestPath.Result result =
                new DagShortestPath().shortestPaths(graph);

        assertEquals(-5, result.getDistance(3));
        assertEquals(Arrays.asList(0, 1, 3), result.getPath(3));
    }

    @Test
    void testMultipleSources() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 2);
        graph.addEdge(1, 4, 7);

        DagShortestPath.Result result =
                new DagShortestPath().shortestPaths(graph);

        assertEquals(0, result.getDistance(0));
        assertEquals(0, result.getDistance(1));
        assertEquals(3, result.getDistance(2));
        assertEquals(5, result.getDistance(3));
        assertEquals(7, result.getDistance(4));

        assertEquals(Arrays.asList(1, 2, 3), result.getPath(3));
    }

    @Test
    void testCycleRejected() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        assertThrows(IllegalStateException.class,
                () -> new DagShortestPath().shortestPaths(graph));
    }


    @Test
    void testEmptyGraph() {
        Graph graph = new Graph(0, true);
        DagShortestPath algorithm = new DagShortestPath();

        DagShortestPath.Result result = algorithm.shortestPaths(graph);

        assertEquals(0, result.sumDistances());
        assertEquals(0, algorithm.getMetrics().edgeScans);
    }


    @Test
    void testRandomDAGsAgainstBellmanFord() {
        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {
            int n = 2 + random.nextInt(19);
            Graph graph = new Graph(n, true);

            int m = n * 4;

            for (int i = 0; i < m; i++) {
                int a = random.nextInt(n);
                int b = random.nextInt(n);

                if (a == b) {
                    continue;
                }

                int from = Math.min(a, b);
                int to = Math.max(a, b);
                int weight = random.nextInt(21) - 10;

                graph.addEdge(from, to, weight);
            }

            DagShortestPath algorithm = new DagShortestPath();
            DagShortestPath.Result result = algorithm.shortestPaths(graph);

            long[] expected = bellmanFordWithSuperSource(graph);

            for (int vertex = 0; vertex < n; vertex++) {
                assertEquals(
                        expected[vertex],
                        result.getDistance(vertex)
                );
            }

            assertEquals(graph.edgeCount(), algorithm.getMetrics().edgeScans);
        }
    }

    private long[] bellmanFordWithSuperSource(Graph graph) {
        int n = graph.vertexCount();
        int[] indegree = new int[n];

        for (Edge edge : graph.getEdges()) {
            indegree[edge.to]++;
        }

        long inf = Long.MAX_VALUE / 4;
        long[] dist = new long[n];

        Arrays.fill(dist, inf);

        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                dist[i] = 0;
            }
        }

        for (int iteration = 0; iteration < n - 1; iteration++) {
            boolean changed = false;

            for (Edge edge : graph.getEdges()) {
                if (dist[edge.from] == inf) {
                    continue;
                }

                long candidate = dist[edge.from] + edge.weight;

                if (candidate < dist[edge.to]) {
                    dist[edge.to] = candidate;
                    changed = true;
                }
            }

            if (!changed) {
                break;
            }
        }

        return dist;
    }
}
