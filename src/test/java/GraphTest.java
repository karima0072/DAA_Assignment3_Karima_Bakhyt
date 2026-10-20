import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    void testUndirectedGraph() {
        Graph graph = new Graph(3, false);

        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 8);

        assertEquals(3, graph.vertexCount());
        assertEquals(2, graph.edgeCount());

        assertEquals(1, graph.neighbors(0).size());
        assertEquals(2, graph.neighbors(1).size());
        assertEquals(1, graph.neighbors(2).size());
    }

    @Test
    void testDirectedGraph() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 5);

        assertEquals(1, graph.edgeCount());
        assertEquals(1, graph.neighbors(0).size());
        assertEquals(0, graph.neighbors(1).size());
    }

    @Test
    void testSelfLoop() {
        Graph graph = new Graph(3, true);

        graph.addEdge(1, 1, 10);

        assertEquals(1, graph.edgeCount());
        assertEquals(1, graph.neighbors(1).size());
    }

    @Test
    void testParallelEdges() {
        Graph graph = new Graph(3, false);

        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 1, 10);

        assertEquals(2, graph.edgeCount());
        assertEquals(2, graph.neighbors(0).size());
    }

    @Test
    void testIsolatedVertex() {
        Graph graph = new Graph(4, false);

        graph.addEdge(0, 1, 5);

        assertEquals(0, graph.neighbors(3).size());
    }

    @Test
    void testInvalidVertex() {
        Graph graph = new Graph(3, false);

        assertThrows(IndexOutOfBoundsException.class,
                () -> graph.addEdge(0, 5, 10));

        assertThrows(IndexOutOfBoundsException.class,
                () -> graph.neighbors(-1));
    }

    @Test
    void testNegativeVertexCount() {
        assertThrows(IllegalArgumentException.class,
                () -> new Graph(-1, false));
    }
    @Test
    void testReadConnectedGraph() throws Exception {
        Graph graph = GraphReader.readFromFile("data/connected.txt");

        assertEquals(5, graph.vertexCount());
        assertEquals(6, graph.edgeCount());
        assertFalse(graph.isDirected());

        assertEquals(2, graph.neighbors(0).size());
        assertEquals(3, graph.neighbors(1).size());
    }

    @Test
    void testReadDisconnectedGraph() throws Exception {
        Graph graph = GraphReader.readFromFile("data/disconnected.txt");

        assertEquals(6, graph.vertexCount());
        assertEquals(3, graph.edgeCount());
        assertFalse(graph.isDirected());

        assertEquals(0, graph.neighbors(5).size());
        assertEquals(1, graph.neighbors(3).size());
    }

    @Test
    void testReadCyclicGraph() throws Exception {
        Graph graph = GraphReader.readFromFile("data/cyclic.txt");

        assertEquals(4, graph.vertexCount());
        assertEquals(4, graph.edgeCount());
        assertTrue(graph.isDirected());

        assertEquals(1, graph.neighbors(0).size());
        assertEquals(2, graph.neighbors(2).size());
    }
}