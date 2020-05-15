import java.util.ArrayList;
import java.util.List;

public class Dijkstra implements IAlgorithm {
    int[][] graph;
    int start, end;

    int[] shortestDistances, parents;
    boolean[] added;

    Dijkstra(int[][] graph, int start, int end) {
        this.graph = graph;
        this.start = start;
        this.end = end;
    }

    private void init() {
        parents = new int[Constants.MAX_VERTICES];
        parents[start] = Constants.NO_PARENT;

        shortestDistances = new int[Constants.MAX_VERTICES];
        added = new boolean[Constants.MAX_VERTICES];

        for (int vertexIndex = 0; vertexIndex < Constants.MAX_VERTICES; vertexIndex++) {
            shortestDistances[vertexIndex] = Integer.MAX_VALUE;
            added[vertexIndex] = false;
        }

        shortestDistances[start] = 0;
    }

    public void fillShortestDistancesArray() {
        for (int i = 1; i < Constants.MAX_VERTICES; i++) {
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0; vertexIndex < Constants.MAX_VERTICES; vertexIndex++) {
                if (!added[vertexIndex] &&
                        shortestDistances[vertexIndex] <
                                shortestDistance) {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }
            added[nearestVertex] = true;

            for (int vertexIndex = 0; vertexIndex < Constants.MAX_VERTICES; vertexIndex++) {

                int edgeDistance = graph[nearestVertex][vertexIndex];

                if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex])) {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance + edgeDistance;
                }
            }
        }

    }

    // gets the nodes in path
    private List<Node> getPathNodes(int currentVertex) {
        if (currentVertex == Constants.NO_PARENT)
            return new ArrayList<>();

        List<Node> ret = getPathNodes(parents[currentVertex]);
        ret.add(new Node(currentVertex));
        return ret;
    }

    @Override
    public Solution getSolution() {
        init();
        fillShortestDistancesArray();
        return new Solution(getPathNodes(end), shortestDistances[end]);
    }
}
