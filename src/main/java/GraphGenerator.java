
import java.util.Random;

public class GraphGenerator {

    private static final int SEED = 42;

    public static Graph generateUndirected(int n, int m) {
        checkParameters(n, m, n - 1);

        Random random = new Random(SEED);
        Graph graph = new Graph(n, false);
        int[] perm = shuffledPermutation(n, random);

        for (int i = 1; i < n; i++) {
            int from = perm[random.nextInt(i)];
            int to = perm[i];
            int weight = 1 + random.nextInt(1000);

            graph.addEdge(from, to, weight);
        }

        for (int i = n - 1; i < m; i++) {
            int from = random.nextInt(n);
            int to = random.nextInt(n);

            while (to == from) {
                to = random.nextInt(n);
            }

            int weight = 1 + random.nextInt(1000);
            graph.addEdge(from, to, weight);
        }

        return graph;
    }


    public static Graph generateDirected(int n, int m) {
        checkParameters(n, m, 0);

        Random random = new Random(SEED);
        Graph graph = new Graph(n, true);

        for (int i = 0; i < m; i++) {
            int from = random.nextInt(n);
            int to = random.nextInt(n);

            while (to == from) {
                to = random.nextInt(n);
            }

            int weight = 1 + random.nextInt(1000);
            graph.addEdge(from, to, weight);
        }

        return graph;
    }



    public static Graph generateDAG(int n, int m) {
        checkParameters(n, m, 0);

        Random random = new Random(SEED);
        Graph graph = new Graph(n, true);

        int[] rank = shuffledPermutation(n, random);

        for (int i = 0; i < m; i++) {
            int from = random.nextInt(n);
            int to = random.nextInt(n);

            while (to == from) {
                to = random.nextInt(n);
            }

            if (rank[from] > rank[to]) {
                int temp = from;
                from = to;
                to = temp;
            }

            int weight = 1 + random.nextInt(1000);
            graph.addEdge(from, to, weight);
        }

        return graph;
    }


    private static int[] shuffledPermutation(int n, Random random) {
        int[] perm = new int[n];

        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }

        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            int temp = perm[i];
            perm[i] = perm[j];
            perm[j] = temp;
        }

        return perm;
    }

    private static void checkParameters(int n, int m, int minimumEdges) {
        if (n <= 0 || m < minimumEdges || m < 0) {
            throw new IllegalArgumentException("Invalid graph size");
        }

        if (n == 1 && m > 0) {
            throw new IllegalArgumentException(
                    "Cannot generate non-loop edges for one vertex"
            );
        }
    }
}
