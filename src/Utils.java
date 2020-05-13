import java.util.Arrays;
import java.util.List;

public class Utils {
    private static int[][] initializeGraph() {
        int[][] graph = new int[Constants.MAX_VERTICES][Constants.MAX_VERTICES];

        for (int[] row: graph)
            Arrays.fill(row, Constants.GRAPH_INITIAL_VALUE);

        return graph;
    }

    public static int[][] undirectedEdgesToGraph(List<Edge> edges) throws Exception {
        int[][] graph = initializeGraph();

        for (Edge curr : edges) {
            if (graph[curr.from][curr.to] != Constants.GRAPH_INITIAL_VALUE)
                throw new Exception("Error repeated edge between " + curr.from + " and " + curr.to + "(Undirected).");
            graph[curr.from][curr.to] = graph[curr.to][curr.from] = curr.cost;
        }

        return graph;
    }

    public static int[][] directedEdgesToGraph(List<Edge> edges) throws Exception {
        int[][] graph = initializeGraph();

        for (Edge curr : edges) {
            if (graph[curr.from][curr.to] != Constants.GRAPH_INITIAL_VALUE)
                throw new Exception("Error repeated edge from " + curr.from + " and " + curr.to + "(Directed).");
            graph[curr.from][curr.to] = curr.cost;
        }

        return graph;
    }



}
