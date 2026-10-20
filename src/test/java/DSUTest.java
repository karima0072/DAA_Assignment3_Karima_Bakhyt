
import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DSUTest {

    @Test
    void testInitialComponents() {
        DSU dsu = new DSU(5);

        assertEquals(5, dsu.components());

        for (int i = 0; i < 5; i++) {
            assertEquals(i, dsu.find(i));
        }
    }

    @Test
    void testUnion() {
        DSU dsu = new DSU(4);

        assertTrue(dsu.union(0, 1));
        assertTrue(dsu.union(2, 3));

        assertEquals(dsu.find(0), dsu.find(1));
        assertEquals(dsu.find(2), dsu.find(3));
        assertEquals(2, dsu.components());
    }

    @Test
    void testAlreadyConnected() {
        DSU dsu = new DSU(3);

        assertTrue(dsu.union(0, 1));
        assertFalse(dsu.union(0, 1));

        assertEquals(2, dsu.components());
        assertEquals(1, dsu.getSuccessfulUnions());
    }

    @Test
    void testAllConnected() {
        DSU dsu = new DSU(5);

        dsu.union(0, 1);
        dsu.union(1, 2);
        dsu.union(2, 3);
        dsu.union(3, 4);

        assertEquals(1, dsu.components());
        assertEquals(dsu.find(0), dsu.find(4));
    }

    @Test
    void testInvalidIndex() {
        DSU dsu = new DSU(3);

        assertThrows(IndexOutOfBoundsException.class,
                () -> dsu.find(5));

        assertThrows(IndexOutOfBoundsException.class,
                () -> dsu.union(-1, 2));
    }

    @Test
    void testEmptyDSU() {
        DSU dsu = new DSU(0);

        assertEquals(0, dsu.components());
    }

    @Test
    void testRandomUnionsAgainstReference() {
        int n = 100;
        DSU dsu = new DSU(n);

        int[] group = new int[n];

        for (int i = 0; i < n; i++) {
            group[i] = i;
        }

        Random random = new Random(42);

        for (int operation = 0; operation < 500; operation++) {
            int a = random.nextInt(n);
            int b = random.nextInt(n);

            int groupA = group[a];
            int groupB = group[b];

            if (groupA != groupB) {
                for (int i = 0; i < n; i++) {
                    if (group[i] == groupB) {
                        group[i] = groupA;
                    }
                }
            }

            dsu.union(a, b);

            int expectedComponents = 0;

            for (int i = 0; i < n; i++) {
                boolean seenBefore = false;

                for (int j = 0; j < i; j++) {
                    if (group[j] == group[i]) {
                        seenBefore = true;
                        break;
                    }
                }

                if (!seenBefore) {
                    expectedComponents++;
                }
            }

            assertEquals(expectedComponents, dsu.components());

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    assertEquals(
                            group[i] == group[j],
                            dsu.find(i) == dsu.find(j)
                    );
                }
            }
        }
    }
}
