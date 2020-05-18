import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dijkstra implements IAlgorithm {
    int[][] graph;
    int start, end;

    int[] shortestDistances, parents;
    boolean[] added;

    int nVertices = Constants.MAX_VERTICES;
    Dijkstra(int[][] graph, int start, int end) {
        this.graph = graph;
        this.start = start;
        this.end = end;
    }

    private void init() {
        parents = new int[nVertices];
        parents[start] = Constants.NO_PARENT;
        Arrays.fill(parents,-1);

        shortestDistances = new int[nVertices];
        added = new boolean[nVertices];

        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
            shortestDistances[vertexIndex] = Integer.MAX_VALUE;
            added[vertexIndex] = false;
        }

        shortestDistances[start] = 0;
    }

    public void fillShortestDistancesArray() {
        for (int i = 1; i < nVertices; i++) {
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
                if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance) {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }
            if (nearestVertex == -1) continue;
            added[nearestVertex] = true;

            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {

                int edgeDistance = graph[nearestVertex][vertexIndex];

                if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex])) {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance + edgeDistance;
                }
            }
        }

    }

    // gets the nodes in path
    private List<Integer> getPathNodes(int currentVertex) {
        if (currentVertex == Constants.NO_PARENT)
            return new ArrayList<>();

        List<Integer> ret = getPathNodes(parents[currentVertex]);
        ret.add(currentVertex);
        return ret;
    }

    @Override
    public Solution getSolution() {
        init();
        fillShortestDistancesArray();
        return new Solution(getPathNodes(end), shortestDistances[end]);
    }
}
