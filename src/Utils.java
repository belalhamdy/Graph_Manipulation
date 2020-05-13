import java.util.Arrays;
import java.util.List;

public class Utils {
    // if there are two edges between same nodes you should take what
    public enum RepeatEdgesType {
        MINIMUM, MAXIMUM
    }

    private static int[][] initializeGraph() {
        int[][] graph = new int[Constants.MAX_VERTICES][Constants.MAX_VERTICES];

        for (int[] row : graph)
            Arrays.fill(row, Constants.GRAPH_INITIAL_VALUE);

        return graph;
    }

    public static int[][] undirectedEdgesToGraph(List<Edge> edges, RepeatEdgesType repeatEdgeType) throws Exception {
        int[][] graph = initializeGraph();

        for (Edge curr : edges) {
            if (graph[curr.from][curr.to] != Constants.GRAPH_INITIAL_VALUE) {
                int oldValue = graph[curr.from][curr.to];
                graph[curr.from][curr.to] = graph[curr.to][curr.from] = handleRepeated(oldValue, curr.cost, repeatEdgeType);
                //throw new Exception("Error repeated edge from " + curr.from + " and " + curr.to + "(Directed).");
            }

            graph[curr.from][curr.to] = graph[curr.to][curr.from] = curr.cost;
        }

        return graph;
    }

    public static int[][] directedEdgesToGraph(List<Edge> edges, RepeatEdgesType repeatEdgeType) throws Exception {
        int[][] graph = initializeGraph();

        for (Edge curr : edges) {
            if (graph[curr.from][curr.to] != Constants.GRAPH_INITIAL_VALUE) {
                int oldValue = graph[curr.from][curr.to];
                graph[curr.from][curr.to] = handleRepeated(oldValue, curr.cost, repeatEdgeType);
                //throw new Exception("Error repeated edge from " + curr.from + " and " + curr.to + "(Directed).");
            }

            graph[curr.from][curr.to] = curr.cost;
        }

        return graph;
    }

    private static int handleRepeated(int oldValue, int newValue, RepeatEdgesType repeatEdgeType) throws Exception {
        if (repeatEdgeType == RepeatEdgesType.MINIMUM) return Math.min(oldValue, newValue);
        else if (repeatEdgeType == RepeatEdgesType.MAXIMUM) return Math.max(oldValue, newValue);
        else throw new Exception("Unsupported repeated edge type.");
    }


}
