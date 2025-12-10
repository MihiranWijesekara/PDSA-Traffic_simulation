package algo;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Algorithm A – Greedy Maximum Bottleneck Path.
 * Uses a priority queue (max-heap) to always expand the node
 * with currently best bottleneck value.
 */
public class GreedyBottleneck {

    public static int maxBottleneck(int[][] capacity, int source, int target) {
        int n = capacity.length;
        int[] bottleneck = new int[n];
        boolean[] visited = new boolean[n];

        Arrays.fill(bottleneck, 0);
        bottleneck[source] = Integer.MAX_VALUE;

        // PQ of {node, bottleneck} sorted by bottleneck descending
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        pq.add(new int[]{source, bottleneck[source]});

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int u = top[0];

            if (visited[u]) continue;
            visited[u] = true;

            if (u == target) break;

            for (int v = 0; v < n; v++) {
                if (capacity[u][v] > 0 && !visited[v]) {
                    int possible = Math.min(bottleneck[u], capacity[u][v]);
                    if (possible > bottleneck[v]) {
                        bottleneck[v] = possible;
                        pq.add(new int[]{v, bottleneck[v]});
                    }
                }
            }
        }

        return bottleneck[target];
    }
}
