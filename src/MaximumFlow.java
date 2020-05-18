import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MaximumFlow implements IAlgorithm {
    int[][] graph;
    int start, end;

    int[][] residualGraph;
    int[] parent;

    MaximumFlow(int[][] graph, int start, int end) {
        this.graph = graph;
        this.start = start;
        this.end = end;
    }

    private void init() {
        this.residualGraph = new int[Constants.MAX_VERTICES][Constants.MAX_VERTICES];
        for (int i = 0; i < residualGraph.length; ++i)
            System.arraycopy(graph[i], 0, residualGraph[i], 0, Constants.MAX_VERTICES);

        parent = new int[Constants.MAX_VERTICES];
    }


    boolean bfs() {
        boolean[] visited = new boolean[Constants.MAX_VERTICES];
        for (int i = 0; i < Constants.MAX_VERTICES; ++i)
            visited[i] = false;

        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;
        parent[start] = -1;


        while (queue.size() != 0) {
            int u = queue.poll();

            for (int v = 0; v < Constants.MAX_VERTICES; v++) {
                if (!visited[v] && residualGraph[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return (visited[end]);
    }

    int fordFulkerson() {
        int u, v;
        int max_flow = 0;
        while (bfs()) {

            int path_flow = Integer.MAX_VALUE;
            for (v = end; v != start; v = parent[v]) {
                u = parent[v];
                path_flow = Math.min(path_flow, residualGraph[u][v]);
            }

            for (v = end; v != start; v = parent[v]) {
                u = parent[v];
                residualGraph[u][v] -= path_flow;
                residualGraph[v][u] += path_flow;
            }

            max_flow += path_flow;
        }

        return max_flow;
    }

    // gets the nodes in path
    private List<Integer> getPathNodes() {
        boolean[] visited = new boolean[Constants.MAX_VERTICES];
        for (int i = 0; i < Constants.MAX_VERTICES; ++i)
            visited[i] = false;

        for (int i = 0; i < Constants.MAX_VERTICES; ++i) {
            for (int j = 0; j < Constants.MAX_VERTICES; ++j) {
                if (graph[i][j] - residualGraph[i][j] > 0)
                    visited[i] = visited[j] = true;
            }
        }

        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < visited.length; ++i) {
            if (visited[i])
                ret.add(i);
        }
        return ret;
    }

    @Override
    public Solution getSolution() {
        init();
        int maxFlow = fordFulkerson();
        return new Solution(getPathNodes(), maxFlow);
    }
}
