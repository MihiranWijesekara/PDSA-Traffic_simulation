package algo;

import java.util.*;

public class EdmondsKarpMaxFlow {

    // BFS-based Ford-Fulkerson (Edmonds-Karp) for max flow
    public static int maxFlow(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        int[][] flow = new int[n][n];
        int maxFlow = 0;

        while (true) {
            int[] parent = new int[n];
            Arrays.fill(parent, -1);
            parent[source] = -2;

            int[] pathCap = new int[n];
            pathCap[source] = Integer.MAX_VALUE;

            Queue<Integer> q = new ArrayDeque<>();
            q.add(source);

            // BFS to find shortest augmenting path
            while (!q.isEmpty() && parent[sink] == -1) {
                int u = q.poll();

                for (int v = 0; v < n; v++) {
                    int residual = capacity[u][v] - flow[u][v];

                    if (parent[v] == -1 && residual > 0) {
                        parent[v] = u;
                        pathCap[v] = Math.min(pathCap[u], residual);

                        if (v == sink) break;
                        q.add(v);
                    }
                }
            }

            if (parent[sink] == -1) break; // no augmenting path

            int add = pathCap[sink];

            // Update residual flow along the path
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                flow[u][v] += add;
                flow[v][u] -= add;
            }

            maxFlow += add;
        }

        return maxFlow;
    }
}
