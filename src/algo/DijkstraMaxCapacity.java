package algo;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Algorithm B – Dijkstra-style Maximum Capacity Path.
 * Another way to compute the same maximum bottleneck value
 * using a Dijkstra-like relaxation rule.
 */
public class DijkstraMaxCapacity {

    public static int maxCapacityPath(int[][] capacity, int source, int target) {
        int n = capacity.length;
        int[] maxCap = new int[n];
        boolean[] visited = new boolean[n];

        Arrays.fill(maxCap, 0);
        maxCap[source] = Integer.MAX_VALUE;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        pq.add(new int[]{source, maxCap[source]});

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int u = top[0];

            if (visited[u]) continue;
            visited[u] = true;

            if (u == target) break;

            for (int v = 0; v < n; v++) {
                if (capacity[u][v] > 0) {
                    int possible = Math.min(maxCap[u], capacity[u][v]);
                    if (possible > maxCap[v]) {
                        maxCap[v] = possible;
                        pq.add(new int[]{v, maxCap[v]});
                    }
                }
            }
        }

        return maxCap[target];
    }
}
