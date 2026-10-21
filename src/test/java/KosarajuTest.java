
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class KosarajuTest {

    @Test
    void testCyclicGraph() throws Exception {
        Graph graph = GraphReader.readFromFile("data/cyclic.txt");

        Kosaraju kosaraju = new Kosaraju();

        assertEquals(2, kosaraju.countSCC(graph));
        assertEquals(2L * graph.edgeCount(),
                kosaraju.getMetrics().edgeScans);
    }

    @Test
    void testDisconnectedGraph() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 0, 3);
        graph.addEdge(2, 3, 4);
        graph.addEdge(3, 2, 5);

        assertEquals(3, new Kosaraju().countSCC(graph));
    }

    @Test
    void testSelfLoopsAndParallelEdges() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 0, 1);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 0, 4);
        graph.addEdge(1, 2, 5);

        assertEquals(2, new Kosaraju().countSCC(graph));
    }

    @Test
    void testEmptyGraph() {
        Graph graph = new Graph(0, true);

        assertEquals(0, new Kosaraju().countSCC(graph));
    }

    @Test
    void testUndirectedGraphRejected() {
        Graph graph = new Graph(3, false);

        assertThrows(IllegalArgumentException.class,
                () -> new Kosaraju().countSCC(graph));
    }

    @Test
    void testRandomGraphsAgainstBruteForce() {
        Random random = new Random(42);

        for (int test = 0; test < 30; test++) {
            int n = 2 + random.nextInt(39);
            Graph graph = new Graph(n, true);

            int m = n * 3;

            for (int i = 0; i < m; i++) {
                graph.addEdge(
                        random.nextInt(n),
                        random.nextInt(n),
                        1
                );
            }

            int expected = bruteForceSCCCount(graph);
            int actual = new Kosaraju().countSCC(graph);

            assertEquals(expected, actual);
        }
    }

    @Test
    void testStackSafety100000() {
        int n = 100_000;
        Graph graph = new Graph(n, true);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        Kosaraju kosaraju = new Kosaraju();

        assertEquals(n, kosaraju.countSCC(graph));
        assertEquals(2L * (n - 1),
                kosaraju.getMetrics().edgeScans);
        assertTrue(kosaraju.getMetrics().maxDepth > 0);
    }

    private int bruteForceSCCCount(Graph graph) {
        int n = graph.vertexCount();
        boolean[][] reachable = new boolean[n][n];

        for (int start = 0; start < n; start++) {
            ArrayDeque<Integer> queue = new ArrayDeque<>();
            queue.add(start);
            reachable[start][start] = true;

            while (!queue.isEmpty()) {
                int vertex = queue.remove();

                for (Edge edge : graph.neighbors(vertex)) {
                    if (!reachable[start][edge.to]) {
                        reachable[start][edge.to] = true;
                        queue.add(edge.to);
                    }
                }
            }
        }

        boolean[] assigned = new boolean[n];
        int components = 0;

        for (int i = 0; i < n; i++) {
            if (assigned[i]) {
                continue;
            }

            components++;

            for (int j = 0; j < n; j++) {
                if (reachable[i][j] && reachable[j][i]) {
                    assigned[j] = true;
                }
            }
        }

        return components;
    }
}
