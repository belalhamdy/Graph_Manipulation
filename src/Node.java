import java.awt.*;

public class Node {
    public enum PortalType {
        START, END
    }

    private int value;
    Color color = Constants.NORMAL_NODE_COLOR;
    boolean isPortal = false;

    Node(int value) {
        this.value = value;
    }

    void setVisited() {
            color = Constants.VISITED_NODE_COLOR;
    }

    void setPortal(PortalType type) {
        if (type == PortalType.START)
            color = Constants.PORTAL_START_NODE_COLOR;
        else if (type == PortalType.END)
            color = Constants.PORTAL_END_NODE_COLOR;
        else return;
        isPortal = true;
    }

    void unsetVisited() {
            color = Constants.NORMAL_NODE_COLOR;
    }

    void unsetPortal() {
        if (isPortal)
            color = Constants.NORMAL_NODE_COLOR;
        isPortal = false;
    }

    // must return int even if it's not an int
    int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
