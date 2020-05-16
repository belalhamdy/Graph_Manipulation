public class Node {
    private int value;
    Node(int value){
        this.value = value;
    }

    // must return int even if it's not an int
    int getValue(){
        return value;
    }
    void setValue(int value){
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
