
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KahnTest {

    @Test
    void testSimpleDAG() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 3, 5);

        Kahn kahn = new Kahn();
        List<Integer> order = kahn.topologicalSort(graph);

        assertEquals(4, order.size());
        assertValidOrder(graph, order);
        assertEquals(graph.edgeCount(), kahn.getMetrics().edgeScans);
    }

    @Test
    void testCyclicGraph() throws Exception {
        Graph graph = GraphReader.readFromFile("data/cyclic.txt");

        assertThrows(IllegalStateException.class,
                () -> new Kahn().topologicalSort(graph));
    }

    @Test
    void testDisconnectedDAG() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        List<Integer> order = new Kahn().topologicalSort(graph);

        assertEquals(5, order.size());
        assertValidOrder(graph, order);
    }

    @Test
    void testEmptyGraph() {
        Graph graph = new Graph(0, true);

        assertTrue(new Kahn().topologicalSort(graph).isEmpty());
    }

    @Test
    void testSelfLoopCycle() {
        Graph graph = new Graph(3, true);

        graph.addEdge(1, 1, 5);

        assertThrows(IllegalStateException.class,
                () -> new Kahn().topologicalSort(graph));
    }

    @Test
    void testUndirectedGraphRejected() {
        Graph graph = new Graph(3, false);

        assertThrows(IllegalArgumentException.class,
                () -> new Kahn().topologicalSort(graph));
    }

    @Test
    void testLargeDAG() {
        int n = 100_000;
        Graph graph = new Graph(n, true);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        Kahn kahn = new Kahn();
        List<Integer> order = kahn.topologicalSort(graph);

        assertEquals(n, order.size());
        assertEquals(n - 1, kahn.getMetrics().edgeScans);
        assertEquals(0, order.get(0));
        assertEquals(n - 1, order.get(n - 1));
    }

    private void assertValidOrder(Graph graph, List<Integer> order) {
        int[] position = new int[graph.vertexCount()];

        for (int i = 0; i < order.size(); i++) {
            position[order.get(i)] = i;
        }

        for (Edge edge : graph.getEdges()) {
            assertTrue(position[edge.from] < position[edge.to]);
        }
    }
}
