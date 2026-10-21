
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphGeneratorTest {

    @Test
    void testUndirectedGraph() {
        Graph graph = GraphGenerator.generateUndirected(100, 400);

        assertEquals(100, graph.vertexCount());
        assertEquals(400, graph.edgeCount());
        assertFalse(graph.isDirected());
    }

    @Test
    void testUndirectedConnected() {
        Graph graph = GraphGenerator.generateUndirected(100, 400);

        boolean[] visited = new boolean[100];
        ArrayDeque<Integer> queue = new ArrayDeque<>();

        visited[0] = true;
        queue.add(0);

        int count = 0;

        while (!queue.isEmpty()) {
            int vertex = queue.remove();
            count++;

            for (Edge edge : graph.neighbors(vertex)) {
                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    queue.add(edge.to);
                }
            }
        }

        assertEquals(100, count);
    }

    @Test
    void testDirectedGraph() {
        Graph graph = GraphGenerator.generateDirected(100, 400);

        assertEquals(100, graph.vertexCount());
        assertEquals(400, graph.edgeCount());
        assertTrue(graph.isDirected());
    }

    @Test
    void testDAG() {
        Graph graph = GraphGenerator.generateDAG(100, 400);

        assertEquals(100, graph.vertexCount());
        assertEquals(400, graph.edgeCount());
        assertTrue(graph.isDirected());

        List<Integer> order = new Kahn().topologicalSort(graph);

        assertEquals(100, order.size());
    }

    @Test
    void testReproducibility() {
        Graph first = GraphGenerator.generateUndirected(100, 400);
        Graph second = GraphGenerator.generateUndirected(100, 400);

        assertEquals(first.edgeCount(), second.edgeCount());

        for (int i = 0; i < first.edgeCount(); i++) {
            Edge a = first.getEdges().get(i);
            Edge b = second.getEdges().get(i);

            assertEquals(a.from, b.from);
            assertEquals(a.to, b.to);
            assertEquals(a.weight, b.weight);
        }
    }

    @Test
    void testInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> GraphGenerator.generateUndirected(10, 5));

        assertThrows(IllegalArgumentException.class,
                () -> GraphGenerator.generateDirected(0, 10));

        assertThrows(IllegalArgumentException.class,
                () -> GraphGenerator.generateDAG(1, 10));
    }
}
