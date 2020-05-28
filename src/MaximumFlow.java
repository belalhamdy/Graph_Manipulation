import java.util.ArrayList;
import java.util.Arrays;
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
            int curr = queue.poll();

            for (int i = 0; i < Constants.MAX_VERTICES; ++i) {
                if (!visited[i] && residualGraph[curr][i] > 0) {
                    queue.add(i);
                    parent[i] = curr;
                    visited[i] = true;
                }
            }
        }

        return (visited[end]);
    }

    int fordFulkerson() {
        int currStartNode;
        int maxFlow = 0;
        while (bfs()) {

            int currPathFlow = Integer.MAX_VALUE;
            for (int i = end; i != start; i = parent[i]) {
                currStartNode = parent[i];
                currPathFlow = Math.min(currPathFlow, residualGraph[currStartNode][i]);
            }

            for (int i = end; i != start; i = parent[i]) {
                currStartNode = parent[i];
                residualGraph[currStartNode][i] -= currPathFlow;
                residualGraph[i][currStartNode] += currPathFlow;
            }

            maxFlow += currPathFlow;
        }

        return maxFlow;
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
