import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GraphReader {

    public static Graph readFromFile(String filename)
            throws FileNotFoundException {

        try (Scanner scanner = new Scanner(new File(filename))) {

            int n = scanner.nextInt();
            int m = scanner.nextInt();
            String type = scanner.next();

            if (!type.equals("directed") && !type.equals("undirected")) {
                throw new IllegalArgumentException(
                        "Unknown graph type: " + type
                );
            }

            boolean directed = type.equals("directed");

            Graph graph = new Graph(n, directed);

            for (int i = 0; i < m; i++) {
                int from = scanner.nextInt();
                int to = scanner.nextInt();
                int weight = scanner.nextInt();

                graph.addEdge(from, to, weight);
            }

            return graph;
        }
    }
}