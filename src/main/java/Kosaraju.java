
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Kosaraju {

    private final GraphMetrics metrics = new GraphMetrics();

    private static class Frame {
        int vertex;
        int nextIndex;

        Frame(int vertex) {
            this.vertex = vertex;
            this.nextIndex = 0;
        }
    }

    public int countSCC(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException("SCC requires a directed graph");
        }

        metrics.reset();

        int n = graph.vertexCount();
        boolean[] visited = new boolean[n];
        List<Integer> finishOrder = new ArrayList<>(n);

        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            ArrayDeque<Frame> stack = new ArrayDeque<>();
            visited[start] = true;
            stack.push(new Frame(start));
            updateDepth(stack.size());

            while (!stack.isEmpty()) {
                Frame frame = stack.peek();
                List<Edge> neighbors = graph.neighbors(frame.vertex);

                if (frame.nextIndex < neighbors.size()) {
                    Edge edge = neighbors.get(frame.nextIndex++);
                    metrics.edgeScans++;

                    int next = edge.to;

                    if (!visited[next]) {
                        visited[next] = true;
                        stack.push(new Frame(next));
                        updateDepth(stack.size());
                    }
                } else {
                    finishOrder.add(frame.vertex);
                    stack.pop();
                }
            }
        }

        Graph reversed = new Graph(n, true);

        for (Edge edge : graph.getEdges()) {
            reversed.addEdge(edge.to, edge.from, edge.weight);
        }

        visited = new boolean[n];
        int components = 0;

        for (int i = finishOrder.size() - 1; i >= 0; i--) {
            int start = finishOrder.get(i);

            if (visited[start]) {
                continue;
            }

            components++;

            ArrayDeque<Integer> stack = new ArrayDeque<>();
            stack.push(start);
            visited[start] = true;
            updateDepth(stack.size());

            while (!stack.isEmpty()) {
                int vertex = stack.pop();

                for (Edge edge : reversed.neighbors(vertex)) {
                    metrics.edgeScans++;

                    if (!visited[edge.to]) {
                        visited[edge.to] = true;
                        stack.push(edge.to);
                        updateDepth(stack.size());
                    }
                }
            }
        }

        return components;
    }

    public GraphMetrics getMetrics() {
        return metrics;
    }

    private void updateDepth(int depth) {
        if (depth > metrics.maxDepth) {
            metrics.maxDepth = depth;
        }
    }
}
