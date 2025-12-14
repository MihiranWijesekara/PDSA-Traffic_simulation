package algo;

import java.util.*;

public class FordFulkersonMaxFlow {

    // Function to find maximum flow from source to sink using DFS
    public static int maxFlow(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        int[][] flow = new int[n][n]; // Flow array to store the flow along each edge
        int maxFlow = 0;

        // Augment the flow while there is a path from source to sink
        while (true) {
            // Find the augmenting path using DFS
            int[] parent = new int[n];
            Arrays.fill(parent, -1);
            parent[source] = -2;
            Stack<Integer> stack = new Stack<>();
            stack.push(source);

            while (!stack.isEmpty()) {
                int u = stack.pop();

                // Go through all adjacent nodes
                for (int v = 0; v < n; v++) {
                    if (parent[v] == -1 && capacity[u][v] - flow[u][v] > 0) {
                        parent[v] = u;
                        if (v == sink) break;
                        stack.push(v);
                    }
                }
            }

            if (parent[sink] == -1) break; // No more augmenting path

            // Find the maximum flow through the path found by DFS
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, capacity[u][v] - flow[u][v]);
            }

            // Update the flow along the path
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                flow[u][v] += pathFlow;
                flow[v][u] -= pathFlow;
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }
}
