import java.util.Arrays;
import java.util.List;

public class AlgorithmsHandler {
    public enum AlgorithmType {
        DIJKSTRA, MAXIMUM_FLOW
    }

    private static int[][] initializeGraph() {
        int[][] graph = new int[Constants.MAX_VERTICES][Constants.MAX_VERTICES];

        for (int[] row : graph)
            Arrays.fill(row, Constants.GRAPH_INITIAL_VALUE);

        return graph;
    }

    private static int[][] undirectedEdgesToGraph(List<Edge> edges, AlgorithmType algorithmType) throws Exception {
        int[][] graph = initializeGraph();

        for (Edge curr : edges) {
            if (graph[curr.from][curr.to] != Constants.GRAPH_INITIAL_VALUE) {
                int oldValue = graph[curr.from][curr.to];
                graph[curr.from][curr.to] = graph[curr.to][curr.from] = handleRepeatedEdge(oldValue, curr.cost, algorithmType);
                //throw new Exception("Error repeated edge from " + curr.from + " and " + curr.to + "(Directed).");
            }

            graph[curr.from][curr.to] = graph[curr.to][curr.from] = curr.cost;
        }

        return graph;
    }

    private static int[][] directedEdgesToGraph(List<Edge> edges, AlgorithmType algorithmType) throws Exception {
        int[][] graph = initializeGraph();

        for (Edge curr : edges) {
            if (graph[curr.from][curr.to] != Constants.GRAPH_INITIAL_VALUE) {
                int oldValue = graph[curr.from][curr.to];
                graph[curr.from][curr.to] = handleRepeatedEdge(oldValue, curr.cost, algorithmType);
                //throw new Exception("Error repeated edge from " + curr.from + " and " + curr.to + "(Directed).");
            }

            graph[curr.from][curr.to] = curr.cost;
        }

        return graph;
    }

    private static int handleRepeatedEdge(int oldValue, int newValue, AlgorithmType algorithmType) throws Exception {
        switch (algorithmType){
            case DIJKSTRA:
                return Math.min(oldValue,newValue); // take minimum cost if dijkstra
            case MAXIMUM_FLOW:
                return oldValue + newValue; // take the sum of them
            default:
                throw new Exception("Unsupported Algorithm is selected.");
        }
    }
    public static List<Edge> executeAlgorithm(List<Edge> edges, Node start, Node end, Edge.GraphType graphType, AlgorithmType algorithmType) throws Exception {
        int[][] graph;
        Solution solution;
        IAlgorithm algorithm;

        switch (graphType){
            case DIRECTED:
                graph = directedEdgesToGraph(edges,algorithmType);
                break;
            case UNDIRECTED:
                graph = undirectedEdgesToGraph(edges,algorithmType);
                break;
            default:
                throw new Exception("Unsupported Graph type.");
        }
        switch (algorithmType){
            case DIJKSTRA:
                algorithm = new Dijkstra(graph,start.getValue(),end.getValue());
                break;
            case MAXIMUM_FLOW:
                algorithm = new MaximumFlow(graph,start.getValue(),end.getValue());
                break;
            default:
                throw new Exception("Unsupported Algorithm is selected.");
        }
        solution = algorithm.getSolution();
        // TODO: Think about returning drawing edge rather than edge
        return null;
    }



}
