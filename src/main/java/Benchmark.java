
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.List;

public class Benchmark {

    private static final int[] SIZES = {
            1000, 3162, 10000, 31623, 100000
    };
    private static final int[] DENSITIES = {4, 20};
    private static final int RUNS = 5;

    private static final long[][] EXPECTED = {
            {147970, 27, 396274},
            {30363, 1, 252411},
            {468911, 137, 1271911},
            {97589, 1, 723157},
            {1486139, 373, 3941828},
            {309550, 1, 2381342},
            {4715515, 1188, 12551891},
            {968517, 1, 7376612},
            {15012094, 3923, 40096539},
            {3056681, 1, 23512087}
    };

    public static void main(String[] args) throws IOException {

        new File("results/plots").mkdirs();

        try (FileWriter writer = new FileWriter("results/results.csv")) {

            writer.write(
                    "algorithm,graph,n,m,time_ms,dsu_ops," +
                            "edge_scans,comparisons,max_depth,result\n"
            );

            int caseIndex = 0;

            for (int n : SIZES) {
                for (int density : DENSITIES) {

                    int m = n * density;
                    String graphType = density == 4 ? "sparse" : "dense";

                    System.out.println("Running n=" + n + ", m=" + m);

                    Graph undirected =
                            GraphGenerator.generateUndirected(n, m);

                    MSTResult kruskalCheck =
                            new Kruskal().findMST(undirected);

                    MSTResult primCheck =
                            new Prim().findMST(undirected);

                    long mstWeight = kruskalCheck.getTotalWeight();

                    if (mstWeight != primCheck.getTotalWeight()) {
                        throw new IllegalStateException(
                                "Kruskal and Prim MST weights do not match"
                        );
                    }

                    benchmarkKruskal(writer, undirected, graphType);
                    benchmarkPrim(writer, undirected, graphType);

                    Graph directed =
                            GraphGenerator.generateDirected(n, m);

                    long sccCount =
                            new Kosaraju().countSCC(directed);

                    benchmarkKosaraju(writer, directed, graphType);

                    Graph dag =
                            GraphGenerator.generateDAG(n, m);

                    long distanceSum =
                            new DagShortestPath()
                                    .shortestPaths(dag)
                                    .sumDistances();

                    benchmarkKahn(writer, dag, graphType);
                    benchmarkDAG(writer, dag, graphType);

                    checkReference(
                            caseIndex,
                            mstWeight,
                            sccCount,
                            distanceSum
                    );

                    writer.flush();

                    System.out.println("Reference values matched.");
                    System.out.println();

                    caseIndex++;
                }
            }
        }

        System.out.println("Benchmark finished.");
    }

    private static double median(double[] times) {
        double[] copy = times.clone();
        Arrays.sort(copy);
        return copy[copy.length / 2];
    }

    private static void writeResult(
            FileWriter writer,
            String algorithm,
            String graphType,
            int n,
            int m,
            double time,
            long dsuOps,
            long edgeScans,
            long comparisons,
            long maxDepth,
            long result
    ) throws IOException {

        writer.write(
                algorithm + "," +
                        graphType + "," +
                        n + "," +
                        m + "," +
                        String.format(Locale.US, "%.6f", time) + "," +
                        dsuOps + "," +
                        edgeScans + "," +
                        comparisons + "," +
                        maxDepth + "," +
                        result + "\n"
        );
    }

    private static void benchmarkKruskal(
            FileWriter writer,
            Graph graph,
            String graphType
    ) throws IOException {

        int n = graph.vertexCount();
        int m = graph.edgeCount();

        double[] times = new double[RUNS];

        new Kruskal().findMST(graph);

        long result = 0;
        long dsuOps = 0;
        long edgeScans = 0;
        long comparisons = 0;

        for (int run = 0; run < RUNS; run++) {

            Kruskal algorithm = new Kruskal();

            long start = System.nanoTime();
            MSTResult mst = algorithm.findMST(graph);
            long end = System.nanoTime();

            times[run] = (end - start) / 1_000_000.0;

            if (run == 0) {
                result = mst.getTotalWeight();
                dsuOps = algorithm.getDsuOps();
                edgeScans = algorithm.getEdgeScans();
                comparisons = algorithm.getComparisons();
            }
        }

        writeResult(
                writer,
                "Kruskal",
                graphType,
                n,
                m,
                median(times),
                dsuOps,
                edgeScans,
                comparisons,
                0,
                result
        );
    }

    private static void benchmarkPrim(
            FileWriter writer,
            Graph graph,
            String graphType
    ) throws IOException {

        int n = graph.vertexCount();
        int m = graph.edgeCount();

        double[] times = new double[RUNS];

        new Prim().findMST(graph);

        long result = 0;
        long edgeScans = 0;
        long comparisons = 0;

        for (int run = 0; run < RUNS; run++) {

            Prim algorithm = new Prim();

            long start = System.nanoTime();
            MSTResult mst = algorithm.findMST(graph);
            long end = System.nanoTime();

            times[run] = (end - start) / 1_000_000.0;

            if (run == 0) {
                result = mst.getTotalWeight();
                edgeScans = algorithm.getEdgeScans();
                comparisons = algorithm.getComparisons();
            }
        }

        writeResult(
                writer,
                "Prim",
                graphType,
                n,
                m,
                median(times),
                0,
                edgeScans,
                comparisons,
                0,
                result
        );
    }

    private static void benchmarkKosaraju(
            FileWriter writer,
            Graph graph,
            String graphType
    ) throws IOException {

        int n = graph.vertexCount();
        int m = graph.edgeCount();

        double[] times = new double[RUNS];

        new Kosaraju().countSCC(graph);

        long result = 0;
        long edgeScans = 0;
        long maxDepth = 0;

        for (int run = 0; run < RUNS; run++) {

            Kosaraju algorithm = new Kosaraju();

            long start = System.nanoTime();
            int components = algorithm.countSCC(graph);
            long end = System.nanoTime();

            times[run] = (end - start) / 1_000_000.0;

            if (run == 0) {
                result = components;
                edgeScans = algorithm.getMetrics().edgeScans;
                maxDepth = algorithm.getMetrics().maxDepth;
            }
        }

        if (edgeScans != 2L * m) {
            throw new IllegalStateException(
                    "Kosaraju edge scans mismatch"
            );
        }

        writeResult(
                writer,
                "Kosaraju",
                graphType,
                n,
                m,
                median(times),
                0,
                edgeScans,
                0,
                maxDepth,
                result
        );
    }

    private static void benchmarkKahn(
            FileWriter writer,
            Graph graph,
            String graphType
    ) throws IOException {

        int n = graph.vertexCount();
        int m = graph.edgeCount();

        double[] times = new double[RUNS];

        new Kahn().topologicalSort(graph);

        long result = 0;
        long edgeScans = 0;

        for (int run = 0; run < RUNS; run++) {

            Kahn algorithm = new Kahn();

            long start = System.nanoTime();
            List<Integer> order = algorithm.topologicalSort(graph);
            long end = System.nanoTime();

            times[run] = (end - start) / 1_000_000.0;


            if (run == 0) {
                result = order.size();
                edgeScans = algorithm.getMetrics().edgeScans;

                validateTopologicalOrder(graph, order);
            }

        }

        if (edgeScans != m) {
            throw new IllegalStateException(
                    "Kahn edge scans mismatch"
            );
        }

        if (result != n) {
            throw new IllegalStateException(
                    "Invalid topological order length"
            );
        }

        writeResult(
                writer,
                "Kahn",
                graphType,
                n,
                m,
                median(times),
                0,
                edgeScans,
                0,
                0,
                result
        );
    }

    private static void benchmarkDAG(
            FileWriter writer,
            Graph graph,
            String graphType
    ) throws IOException {

        int n = graph.vertexCount();
        int m = graph.edgeCount();

        double[] times = new double[RUNS];

        new DagShortestPath().shortestPaths(graph);

        long result = 0;
        long edgeScans = 0;

        for (int run = 0; run < RUNS; run++) {

            DagShortestPath algorithm = new DagShortestPath();

            long start = System.nanoTime();

            DagShortestPath.Result shortest =
                    algorithm.shortestPaths(graph);

            long end = System.nanoTime();

            times[run] = (end - start) / 1_000_000.0;

            if (run == 0) {
                result = shortest.sumDistances();
                edgeScans = algorithm.getMetrics().edgeScans;
            }
        }

        if (edgeScans != m) {
            throw new IllegalStateException(
                    "DAG edge scans mismatch"
            );
        }

        writeResult(
                writer,
                "DagShortestPath",
                graphType,
                n,
                m,
                median(times),
                0,
                edgeScans,
                0,
                0,
                result
        );
    }

    private static void validateTopologicalOrder(
            Graph graph,
            List<Integer> order
    ) {
        int n = graph.vertexCount();

        if (order.size() != n) {
            throw new IllegalStateException(
                    "Invalid topological order length"
            );
        }

        int[] position = new int[n];
        Arrays.fill(position, -1);

        for (int i = 0; i < order.size(); i++) {
            int vertex = order.get(i);

            if (vertex < 0 || vertex >= n || position[vertex] != -1) {
                throw new IllegalStateException(
                        "Invalid or duplicate vertex in topological order"
                );
            }

            position[vertex] = i;
        }

        for (Edge edge : graph.getEdges()) {
            if (position[edge.from] >= position[edge.to]) {
                throw new IllegalStateException(
                        "Invalid topological order: edge " +
                                edge.from + " -> " + edge.to
                );
            }
        }
    }


    private static void checkReference(
            int index,
            long mstWeight,
            long sccCount,
            long distanceSum
    ) {

        if (mstWeight != EXPECTED[index][0] ||
                sccCount != EXPECTED[index][1] ||
                distanceSum != EXPECTED[index][2]) {

            throw new IllegalStateException(
                    "Reference values mismatch at case " + index +
                            ". Expected: " + Arrays.toString(EXPECTED[index]) +
                            ", Actual: [" + mstWeight + ", " +
                            sccCount + ", " + distanceSum + "]"
            );
        }
    }
}
