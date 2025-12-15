package test.algo;

import algo.EdmondsKarpMaxFlow;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EdmondsKarpMaxFlowTest {

    @Test
    void testSimpleSinglePath() {
        int[][] cap = {
                {0, 10, 0},
                {0, 0, 7},
                {0, 0, 0}
        };

        int result = EdmondsKarpMaxFlow.maxFlow(cap, 0, 2);
        assertEquals(7, result);
    }

    @Test
    void testTwoParallelPaths() {
        int[][] cap = {
                {0, 5, 7, 0},
                {0, 0, 0, 5},
                {0, 0, 0, 7},
                {0, 0, 0, 0}
        };

        int result = EdmondsKarpMaxFlow.maxFlow(cap, 0, 3);
        assertEquals(12, result);
    }

    @Test
    void testClassicCLRSExample() {
        int[][] cap = {
                {0,16,13, 0, 0, 0},
                {0, 0,10,12, 0, 0},
                {0, 4, 0, 0,14, 0},
                {0, 0, 9, 0, 0,20},
                {0, 0, 0, 7, 0, 4},
                {0, 0, 0, 0, 0, 0}
        };

        int result = EdmondsKarpMaxFlow.maxFlow(cap, 0, 5);
        assertEquals(23, result);
    }

    @Test
    void testNoPathToSink() {
        int[][] cap = {
                {0, 5, 0, 0},
                {0, 0, 3, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int result = EdmondsKarpMaxFlow.maxFlow(cap, 0, 3);
        assertEquals(0, result);
    }

    @Test
    void testSourceEqualsSink() {
        int[][] cap = {
                {0, 1},
                {0, 0}
        };

        int result = EdmondsKarpMaxFlow.maxFlow(cap, 0, 0);
        assertEquals(0, result);
    }

    @Test
    void testInputNotMutated() {
        int[][] cap = {
                {0, 3, 2, 0},
                {0, 0, 0, 2},
                {0, 1, 0, 3},
                {0, 0, 0, 0}
        };

        int[][] original = deepCopy(cap);

        EdmondsKarpMaxFlow.maxFlow(cap, 0, 3);

        assertArrayEquals(original, cap);
    }

    private static int[][] deepCopy(int[][] a) {
        int[][] b = new int[a.length][];
        for (int i = 0; i < a.length; i++) {
            b[i] = Arrays.copyOf(a[i], a[i].length);
        }
        return b;
    }
}
