public class Edge {
    int from, to,cost;
    public enum GraphType{
        directed,undirected
    }
    Edge(Node from, Node to, int cost, GraphType type) throws Exception {
        this.from = from.getValue();
        this.to = to.getValue();
        this.cost = cost;

        /*// makes sure that always the from is smallest
        if (type == GraphType.undirected && from.getValue() > to.getValue()){
            this.from = to.getValue();
            this.to = from.getValue();
        }*/

        if (cost <= 0 )
            throw new Exception("Cost is positive number only.");
        if (this.from >= Constants.MAX_VERTICES || this.to >= Constants.MAX_VERTICES)
            throw new Exception("Please enter a value between 0 and " + Constants.MAX_VERTICES + ".");
    }

}
