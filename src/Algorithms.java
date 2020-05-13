import java.util.List;

public class Algorithms {
    public enum AlgorithmType {
            DIJKSTRA, MAXIMUM_FLOW
    }
    private static Utils.RepeatEdgesType getProperRepeatedEdgeType(AlgorithmType algorithmType) throws Exception {
        switch (algorithmType){
            case DIJKSTRA:
                return Utils.RepeatEdgesType.MINIMUM; // take minimum cost if dijkstra
            case MAXIMUM_FLOW:
                return Utils.RepeatEdgesType.MAXIMUM; // take minimum cost if max flow
            default:
                throw new Exception("Unsupported Algorithm is selected.");
        }
    }
    public static List<Edge> executeAlgorithm(List<Edge> edges, Node start, Node end, Edge.GraphType graphType, AlgorithmType algorithmType) throws Exception {
        int[][] graph;
        Solution solution;
        Utils.RepeatEdgesType repeatEdgesType = getProperRepeatedEdgeType(algorithmType);

        switch (graphType){
            case DIRECTED:
                graph = Utils.directedEdgesToGraph(edges,repeatEdgesType);
                break;
            case UNDIRECTED:
                graph = Utils.undirectedEdgesToGraph(edges,repeatEdgesType);
                break;
            default:
                throw new Exception("Unsupported Graph type.");
        }
        switch (algorithmType){
            case DIJKSTRA:
                solution = Dijkstra.shortestPathFromStartToEndDijkstra(graph,start.getValue(),end.getValue());
                break;
            case MAXIMUM_FLOW:
                solution = null;
                break;
            default:
                throw new Exception("Unsupported Algorithm is selected.");
        }
        // TODO: Think about returning drawing edge rather than edge
        return null;
    }


}
