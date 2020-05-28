public class Edge {
    int from, to,cost;
    public enum GraphType{
        DIRECTED,UNDIRECTED
    }
    Edge(Node from, Node to, int cost) {
        this.from = from.getValue();
        this.to = to.getValue();
        this.cost = cost;
    }
    Edge(int from, int to, int cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }




}
