package test.algo;

import algo.FordFulkersonMaxFlow;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FordFulkersonMaxFlowTest {

    @Test
    void testSimpleSinglePath() {
        // 0 -> 1 -> 2
        int[][] cap = {
                {0, 10, 0},
                {0,  0, 7},
                {0,  0, 0}
        };

        int result = FordFulkersonMaxFlow.maxFlow(cap, 0, 2);
        assertEquals(7, result);
    }

    @Test
    void testTwoParallelPaths() {
        // 0->1->3 (5) and 0->2->3 (7) => max = 12
        int[][] cap = {
                {0, 5, 7, 0},
                {0, 0, 0, 5},
                {0, 0, 0, 7},
                {0, 0, 0, 0}
        };

        int result = FordFulkersonMaxFlow.maxFlow(cap, 0, 3);
        assertEquals(12, result);
    }

    @Test
    void testClassicCLRSExample() {
        // Classic example (expected max flow = 23)
        int[][] cap = {
                //0  1  2  3  4  5
                { 0,16,13, 0, 0, 0}, // 0
                { 0, 0,10,12, 0, 0}, // 1
                { 0, 4, 0, 0,14, 0}, // 2
                { 0, 0, 9, 0, 0,20}, // 3
                { 0, 0, 0, 7, 0, 4}, // 4
                { 0, 0, 0, 0, 0, 0}  // 5
        };

        int result = FordFulkersonMaxFlow.maxFlow(cap, 0, 5);
        assertEquals(23, result);
    }

    @Test
    void testNoPathToSink() {
        // sink 3 is disconnected => 0
        int[][] cap = {
                {0, 5, 0, 0},
                {0, 0, 3, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int result = FordFulkersonMaxFlow.maxFlow(cap, 0, 3);
        assertEquals(0, result);
    }

    @Test
    void testSourceEqualsSink() {
        int[][] cap = {
                {0, 1},
                {0, 0}
        };

        // Conventionally, flow from node to itself = 0
        int result = FordFulkersonMaxFlow.maxFlow(cap, 0, 0);
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

        FordFulkersonMaxFlow.maxFlow(cap, 0, 3);

        assertArrayEquals(original, cap, "capacity matrix should not be modified");
    }

    private static int[][] deepCopy(int[][] a) {
        int[][] b = new int[a.length][];
        for (int i = 0; i < a.length; i++) {
            b[i] = Arrays.copyOf(a[i], a[i].length);
        }
        return b;
    }
}