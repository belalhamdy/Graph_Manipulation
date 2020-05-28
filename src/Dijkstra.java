import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dijkstra implements IAlgorithm {
    int[][] graph;
    int start, end;

    int[] shortestDistances, parent;
    boolean[] visited;

    int nVertices = Constants.MAX_VERTICES;

    Dijkstra(int[][] graph, int start, int end) {
        this.graph = graph;
        this.start = start;
        this.end = end;
    }

    private void init() {
        parent = new int[nVertices];
        parent[start] = Constants.NO_PARENT;
        Arrays.fill(parent, -1);

        shortestDistances = new int[nVertices];
        visited = new boolean[nVertices];

        for (int i = 0; i < nVertices; ++i) {
            shortestDistances[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }

        shortestDistances[start] = 0;
    }

    public void fillShortestDistancesArray() {
        for (int curr = 1; curr < nVertices; ++curr) {
            int nearest = -1;
            int minDistance = Integer.MAX_VALUE;
            for (int i = 0; i < nVertices; ++i) {
                if (!visited[i] && shortestDistances[i] < minDistance) {
                    nearest = i;
                    minDistance = shortestDistances[i];
                }
            }

            if (nearest == -1) continue;
            visited[nearest] = true;

            for (int i = 0; i < nVertices; ++i) {

                int edgeDistance = graph[nearest][i];

                if (edgeDistance > 0 && ((minDistance + edgeDistance) < shortestDistances[i])) {
                    parent[i] = nearest;
                    shortestDistances[i] = minDistance + edgeDistance;
                }
            }
        }

    }

    // gets the nodes in path
    private List<Integer> getPathNodes(int currentVertex) {
        if (currentVertex == Constants.NO_PARENT)
            return new ArrayList<>();

        List<Integer> ret = getPathNodes(parent[currentVertex]);
        ret.add(currentVertex);
        return ret;
    }

    private List<Edge> getVisitedEdges(List<Integer> visitedNodes) {
        List<Edge> ret = new ArrayList<>();
        for(int i = 0 ; i < visitedNodes.size() - 1 ; ++i){
            ret.add(new Edge(GUI.nodes[visitedNodes.get(i)],GUI.nodes[visitedNodes.get(i+1)],graph[i][i+1]));
        }
        return ret;
    }

    @Override
    public Solution getSolution() {
        init();
        fillShortestDistancesArray();
        List<Integer> visitedNodes = getPathNodes(end);
        List<Edge> visitedEdges = getVisitedEdges(visitedNodes);
        return new Solution(visitedNodes, visitedEdges, shortestDistances[end]);
    }
}
