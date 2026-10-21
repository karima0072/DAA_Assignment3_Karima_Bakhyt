
public class GraphMetrics {

    public long dsuOps;
    public long edgeScans;
    public long comparisons;
    public long maxDepth;

    public void reset() {
        dsuOps = 0;
        edgeScans = 0;
        comparisons = 0;
        maxDepth = 0;
    }
}
