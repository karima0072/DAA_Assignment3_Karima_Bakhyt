
import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {

    @Test
    void testConnectedGraph() throws Exception {
        Graph graph = GraphReader.readFromFile("data/connected.txt");

        MSTResult kruskal = new Kruskal().findMST(graph);
        MSTResult prim = new Prim().findMST(graph);

        assertEquals(11, kruskal.getTotalWeight());
        assertEquals(11, prim.getTotalWeight());
        assertEquals(1, kruskal.getComponents());
        assertEquals(1, prim.getComponents());
        assertEquals(4, kruskal.edgeCount());
        assertEquals(4, prim.edgeCount());
    }

    @Test
    void testDisconnectedGraph() throws Exception {
        Graph graph = GraphReader.readFromFile("data/disconnected.txt");

        MSTResult kruskal = new Kruskal().findMST(graph);
        MSTResult prim = new Prim().findMST(graph);

        assertEquals(13, kruskal.getTotalWeight());
        assertEquals(13, prim.getTotalWeight());
        assertEquals(3, kruskal.getComponents());
        assertEquals(3, prim.getComponents());
        assertEquals(3, kruskal.edgeCount());
        assertEquals(3, prim.edgeCount());
    }

    @Test
    void testEmptyGraph() {
        Graph graph = new Graph(0, false);

        MSTResult kruskal = new Kruskal().findMST(graph);
        MSTResult prim = new Prim().findMST(graph);

        assertEquals(0, kruskal.getTotalWeight());
        assertEquals(0, prim.getTotalWeight());
        assertEquals(0, kruskal.getComponents());
        assertEquals(0, prim.getComponents());
    }

    @Test
    void testSelfLoopsAndParallelEdges() {
        Graph graph = new Graph(3, false);

        graph.addEdge(0, 0, 1);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 4);
        graph.addEdge(0, 2, 20);

        MSTResult kruskal = new Kruskal().findMST(graph);
        MSTResult prim = new Prim().findMST(graph);

        assertEquals(7, kruskal.getTotalWeight());
        assertEquals(7, prim.getTotalWeight());
        assertEquals(2, kruskal.edgeCount());
        assertEquals(2, prim.edgeCount());
    }

    @Test
    void testDirectedGraphRejected() {
        Graph graph = new Graph(3, true);

        assertThrows(IllegalArgumentException.class,
                () -> new Kruskal().findMST(graph));

        assertThrows(IllegalArgumentException.class,
                () -> new Prim().findMST(graph));
    }

    @Test
    void testHundredRandomConnectedGraphs() {
        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {
            int n = 2 + random.nextInt(49);
            Graph graph = new Graph(n, false);

            for (int i = 1; i < n; i++) {
                graph.addEdge(
                        random.nextInt(i),
                        i,
                        1 + random.nextInt(100)
                );
            }

            int extraEdges = n * 3;

            for (int i = 0; i < extraEdges; i++) {
                int a = random.nextInt(n);
                int b = random.nextInt(n);

                if (a != b) {
                    graph.addEdge(a, b, 1 + random.nextInt(100));
                }
            }

            MSTResult kruskal = new Kruskal().findMST(graph);
            MSTResult prim = new Prim().findMST(graph);

            assertEquals(
                    kruskal.getTotalWeight(),
                    prim.getTotalWeight()
            );

            assertEquals(n - 1, kruskal.edgeCount());
            assertEquals(n - 1, prim.edgeCount());
            assertEquals(1, kruskal.getComponents());
            assertEquals(1, prim.getComponents());
        }
    }
}
