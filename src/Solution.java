import java.util.List;

public class Solution {
    List<Integer> nodes;
    List<Edge> edges;
    int solutionCost;
    boolean repeatEdge ;
    Solution(List<Integer> nodes,List<Edge> edges, int solutionCost){
        this.nodes = nodes;
        this.solutionCost = solutionCost;
        this.edges = edges;
    }
}
