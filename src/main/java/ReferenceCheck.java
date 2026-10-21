
public class ReferenceCheck {

    public static void main(String[] args) {
        int[] sizes = {1000, 3162, 10000, 31623, 100000};
        int[] densities = {4, 20};

        for (int n : sizes) {
            for (int density : densities) {
                int m = n * density;

                System.out.println("Checking n=" + n + ", m=" + m);

                Graph undirected = GraphGenerator.generateUndirected(n, m);

                MSTResult kruskal = new Kruskal().findMST(undirected);
                MSTResult prim = new Prim().findMST(undirected);

                if (kruskal.getTotalWeight() != prim.getTotalWeight()) {
                    throw new IllegalStateException("MST weights do not match");
                }

                Graph directed = GraphGenerator.generateDirected(n, m);
                int scc = new Kosaraju().countSCC(directed);

                Graph dag = GraphGenerator.generateDAG(n, m);
                long distanceSum = new DagShortestPath()
                        .shortestPaths(dag)
                        .sumDistances();

                System.out.println("MST weight: " + kruskal.getTotalWeight());
                System.out.println("SCC count: " + scc);
                System.out.println("DAG sum: " + distanceSum);
                System.out.println();
            }
        }

        System.out.println("Reference check finished.");
    }
}
