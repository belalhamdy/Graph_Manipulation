import java.util.List;

public class Algorithms {
    public enum AlgorithmType {
            Dijkstra, MaximumFlow
    }
    public static List<Edge> executeAlgorithm(List<Edge> edges, Edge.GraphType graphType, AlgorithmType algorithmType) throws Exception {
        int[][] graph;
        List<Edge> edgesResult;
        switch (graphType){
            case directed:
                graph = Utils.directedEdgesToGraph(edges);
                break;
            case undirected:
                graph = Utils.undirectedEdgesToGraph(edges);
                break;
            default:
                throw new Exception("Unsupported Graph type.");
        }
        switch (algorithmType){
            case Dijkstra:
                // TODO: Pass Graph to dijkstra and fill edgesResult with List<Edge>
                edgesResult = null;
                break;
            case MaximumFlow:
                // TODO: Pass Graph to Max Flow and fill edgesResult with List<Edge>
                edgesResult = null;
                break;
            default:
                throw new Exception("Unsupported Algorithm is selected.");
        }
        // TODO: Think about returning drawing edge rather than edge
        return edgesResult;
    }


}
