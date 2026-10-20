
public class DSU {

    private int[] parent;
    private int[] rank;
    private int count;

    private long findCalls;
    private long successfulUnions;

    public DSU(int n) {
        makeSet(n);
    }

    public void makeSet(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Negative size");
        }

        parent = new int[n];
        rank = new int[n];
        count = n;

        findCalls = 0;
        successfulUnions = 0;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        checkIndex(x);
        findCalls++;

        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }

        return parent[x];
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        count--;
        successfulUnions++;
        return true;
    }

    public int components() {
        return count;
    }

    public long getFindCalls() {
        return findCalls;
    }

    public long getSuccessfulUnions() {
        return successfulUnions;
    }

    public void resetCounters() {
        findCalls = 0;
        successfulUnions = 0;
    }

    private void checkIndex(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException("Invalid element: " + x);
        }
    }
}
