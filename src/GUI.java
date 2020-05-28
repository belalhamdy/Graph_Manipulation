import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GUI {
    private JPanel mainPnl;
    private JRadioButton directedRadio;
    private JRadioButton undirectedRadio;
    private JComboBox<String> startVertexCbx;
    private JComboBox<String> endVertexCbx;
    private JButton dijkstraShortestPathButton;
    private JButton fordFulkersonMaximumFlowButton;
    private JTable edgesTable;
    private JTextArea resultsTxt;
    private JScrollPane graphPnl;
    private JScrollPane edgesPnl;
    private JButton clearAllButton;
    private JRadioButton allAtOnceRadioButton;
    private JRadioButton stepByStepRadioButton;

    Graph<Node, Edge> g;
    VisualizationViewer<Node, Edge> vv;
    Layout<Node, Edge> layout;

    Node testNode1 = new Node(1), testNode2 = new Node(2);
    Edge testEdge = new Edge(testNode1, testNode2, 0);

    Node[] nodes;
    Edge[] edges,edgesCpy;
    int[] nodeFreq;

    boolean isDirected = true;
    public static final String[] columns = {
            "Id", "From", "To", "Cost"
    };

    public GUI() {
        JFrame form = new JFrame("Graph Manipulation");
        form.setMinimumSize(new Dimension(1150, 800));
        form.setResizable(false);
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setContentPane(mainPnl);
        form.pack();
        form.setVisible(true);
        form.setLocationRelativeTo(null);
//        --------------------------------------------------------
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
//        --------------------------------------------------------
        endVertexCbx.setMinimumSize(Constants.comboBoxDimension);
        endVertexCbx.setPreferredSize(Constants.comboBoxDimension);
        endVertexCbx.setMaximumSize(Constants.comboBoxDimension);

        startVertexCbx.setMinimumSize(Constants.comboBoxDimension);
        startVertexCbx.setPreferredSize(Constants.comboBoxDimension);
        startVertexCbx.setMaximumSize(Constants.comboBoxDimension);

        startVertexCbx.addActionListener(e -> handleComboBoxChanges());
        endVertexCbx.addActionListener(e -> handleComboBoxChanges());

        clearAllButton.addActionListener(e -> clearAll());
//        --------------------------------------------------------
        ButtonGroup directionRadioGroup = new ButtonGroup();
        directionRadioGroup.add(directedRadio);
        directionRadioGroup.add(undirectedRadio);
        directedRadio.setSelected(true);

        directedRadio.addActionListener(e -> handleDirectedRadioChanges(true));
        undirectedRadio.addActionListener(e -> handleDirectedRadioChanges(false));
//        --------------------------------------------------------
        ButtonGroup representationRadioGroup = new ButtonGroup();
        representationRadioGroup.add(allAtOnceRadioButton);
        representationRadioGroup.add(stepByStepRadioButton);
        allAtOnceRadioButton.setSelected(true);
//        --------------------------------------------------------
        graphPnl.setMinimumSize(Constants.graphPnlDimension);
        graphPnl.setMaximumSize(Constants.graphPnlDimension);
        graphPnl.setSize(Constants.graphPnlDimension);

        JPanel centerPanel = new JPanel(new GridLayout(0, 1));
        centerPanel.setSize(Constants.graphPnlDimension);
        graphPnl.setViewportView(centerPanel);
//        --------------------------------------------------------
        edgesPnl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                edgesPnl.requestFocus();
            }
        });
//        --------------------------------------------------------
        dijkstraShortestPathButton.addActionListener(e -> handleAlgorithmExecution(AlgorithmsHandler.AlgorithmType.DIJKSTRA));
        fordFulkersonMaximumFlowButton.addActionListener(e -> handleAlgorithmExecution(AlgorithmsHandler.AlgorithmType.MAXIMUM_FLOW));
//        --------------------------------------------------------
        initComboBoxes();
        initTable();
        initNodesAndEdges();
        initGraph();
        //buttonsVisibility(false);
    }

    private void initComboBoxes() {
        startVertexCbx.addItem(Constants.initialComboBoxText);
        endVertexCbx.addItem(Constants.initialComboBoxText);
    }


    private void initNodesAndEdges() {
        nodes = new Node[Constants.MAX_VERTICES];
        nodeFreq = new int[nodes.length];
        for (int i = 0; i < nodes.length; ++i) nodes[i] = new Node(i);

        edges = new Edge[Constants.MAX_EDGES];

        testEdge = new Edge(nodes[0], nodes[1], -5);
    }

    private void initGraph() {
        JDialog jDialog = new JDialog();
        jDialog.setLayout(new GridBagLayout());
        jDialog.add(new JLabel("Please wait until test edge loads..."));
        jDialog.setAlwaysOnTop(true);
        jDialog.setMinimumSize(new Dimension(200, 50));
        jDialog.setResizable(false);
        jDialog.setModal(false);
        jDialog.setUndecorated(true);
        jDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);


        g = new SparseMultigraph<>();
        layout = new CircleLayout<>(g);

        layout.setSize(Constants.graphPnlDimension);
        vv = new VisualizationViewer<>(layout);

        Transformer<Node, Paint> vertexColor = i -> vv.getPickedVertexState().isPicked(i) ? Constants.PICKED_NODE_COLOR : i.color;

        vv.setSize(Constants.graphPnlDimension); //Sets the viewing area size
        vv.getRenderContext().setVertexLabelTransformer(String::valueOf);
        vv.getRenderContext().setEdgeLabelTransformer(s -> s == testEdge ? "Initial Test Edge" : String.valueOf(s.cost));
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);

        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<>());

        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<Integer, Number>();
        vv.setGraphMouse(graphMouse);
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);

        JPanel view = (JPanel) graphPnl.getViewport().getView();
        view.add(panel);
        view.validate();


        //----------------------------------------------------
        testNode1.color = Color.ORANGE;
        testNode2.color = Color.ORANGE;
        g.addEdge(testEdge, testNode2, testNode1);
        layout = new CircleLayout<>(g);
        vv.setGraphLayout(layout);
        vv.repaint();
        //----------------------------------------------------
        jDialog.setVisible(false);

        //vv.getPickedVertexState().isPicked(); // you will need it
    }

    private void refreshGraphColors() {
        vv.repaint();
    }

    private void refreshGraph() {
        if (testEdge != null) {
            g.removeEdge(testEdge);
            g.removeVertex(testNode1);
            g.removeVertex(testNode2);
            testEdge = null;
            testNode1 = testNode2 = null;
        }
        Arrays.fill(nodeFreq, 0);
        for (Edge curr : edges) {
            if (curr != null) {
                g.removeEdge(curr);
                g.addEdge(curr, nodes[curr.from], nodes[curr.to], isDirected ? EdgeType.DIRECTED : EdgeType.UNDIRECTED);
                nodeFreq[curr.from]++;
                nodeFreq[curr.to]++;
            }
        }
        layout = new CircleLayout<>(g);
        vv.setGraphLayout(layout);
        vv.repaint();
        refreshComboBoxes();

    }

    private void removeEdgeFromGraph(Edge e, int idx) {
        if (e != null) {
            g.removeEdge(e);
            g.removeVertex(nodes[e.from]);
            g.removeVertex(nodes[e.to]);
        }
        edges[idx] = null;
    }

    private void removeAllEdgesAndNodes() {
        for (int i = 0; i < edges.length; ++i) {
            removeEdgeFromGraph(edges[i], i);
        }
    }

    private void clearAll() {
        removeAllEdgesAndNodes();
        for (int i = 0; i < edgesTable.getRowCount(); ++i) {
            edgesTable.setValueAt(null, i, 1);
            edgesTable.setValueAt(null, i, 2);
            edgesTable.setValueAt(null, i, 3);
        }
        resultsTxt.setText("");
        refreshGraph();
    }

    private void handleDirectedRadioChanges(boolean isDirected) {
        if (this.isDirected == isDirected) return;
        this.isDirected = isDirected;
        refreshGraph();
    }

    private int getStartComboBoxValue() throws Exception {
        if (startVertexCbx.getSelectedItem() == null) throw new Exception("No value is selected in start vertex.");
        String startItem = String.valueOf(startVertexCbx.getSelectedItem());
        if (startItem.equals(Constants.initialComboBoxText))
            throw new Exception("Please enter edges in the table to start.");
        try {
            return Integer.parseInt(startItem);
        } catch (Exception e) {
            throw new Exception("Unsupported datatype in start vertex");
        }
    }

    private int getEndComboBoxValue() throws Exception {
        if (endVertexCbx.getSelectedItem() == null) throw new Exception("No value is selected in end vertex.");
        String endItem = String.valueOf(endVertexCbx.getSelectedItem());
        if (endItem.equals(Constants.initialComboBoxText))
            throw new Exception("Please enter edges in the table to start.");
        try {
            return Integer.parseInt(endItem);
        } catch (Exception e) {
            throw new Exception("Unsupported datatype in end vertex");
        }
    }

    /*private void buttonsVisibility(boolean isVisible){
        isVisible = true;
        dijkstraShortestPathButton.setEnabled(isVisible);
        fordFulkersonMaximumFlowButton.setEnabled(isVisible);
    }*/
    private void handleComboBoxChanges() {
        int startVal, endVal;
        //buttonsVisibility(false);
        try {
            startVal = getStartComboBoxValue();
            endVal = getEndComboBoxValue();
        } catch (Exception ex) {
            return;
        }

        clearNodesColors();
        nodes[startVal].setPortal(Node.PortalType.START);
        nodes[endVal].setPortal(Node.PortalType.END);

        //if (startVal != endVal) buttonsVisibility(true);

        refreshGraphColors();


    }


    private void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, "Error in " + title, JOptionPane.ERROR_MESSAGE);
    }

    private void showWarningMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, "Warning in " + title, JOptionPane.WARNING_MESSAGE);
    }

    private void handleAlgorithmExecution(AlgorithmsHandler.AlgorithmType algorithmType) {
        refreshGraph();
        Solution solution;
        try {
            int startVal = getStartComboBoxValue();
            int endVal = getEndComboBoxValue();

            if (startVal == endVal) throw new Exception("Start node cannot be same as end node.");
            startVertexCbx.setSelectedIndex(startVertexCbx.getSelectedIndex()); // refresh colors from previous execution

            Edge.GraphType graphType = isDirected ? Edge.GraphType.DIRECTED : Edge.GraphType.UNDIRECTED;

            solution = AlgorithmsHandler.executeAlgorithm(edges, nodes[startVal], nodes[endVal], graphType, algorithmType);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Algorithm Execution", e.getMessage());
            return;
        }
        String message = "";
        switch (algorithmType) {
            case DIJKSTRA:
                if (solution.repeatEdge)
                    showWarningMessage("Dijkstra execution", "You have entered multiple edges between same nodes so Dijkstra will take the minimum cost.");
                message = "Dijkstra Shortest Path Cost is: " + solution.solutionCost;
                break;
            case MAXIMUM_FLOW:
                if (solution.repeatEdge)
                    showWarningMessage("Ford-Fulkerson execution", "You have entered multiple edges between same nodes so Ford-Fulkerson will take the sum of their costs.");
                message = "Maximum Flow by Ford-Fulkerson is: " + solution.solutionCost;
                break;
        }
        resultsTxt.setText(message);

        setListOfNodesVisited(solution.nodes);
        if (stepByStepRadioButton.isSelected()) handleStepByStepRepresentation(solution);
    }


    private void handleStepByStepRepresentation(Solution solution) {
        edgesCpy = edges.clone();
        removeAllEdgesAndNodes();
        java.util.Timer timer = new Timer();
        timer.schedule(new addEdgesTimer(solution.edges), 1000, 1000);
    }
    public class addEdgesTimer extends TimerTask {
        List<Edge> edgeList;
        int currIndex;
        boolean done;

        addEdgesTimer(List<Edge> edgeList) {
            this.edgeList = edgeList;
            currIndex = 0;
            done = false;
        }

        void addNextEdge() {
            if (currIndex >= edgeList.size()) {
                done = true;
                return;
            }

            Edge currEdge = edgeList.get(currIndex);
            Node from = nodes[currEdge.from], to = nodes[currEdge.to];

            edges[currIndex] = new Edge(from, to, currEdge.cost);
            Edge curr = edges[currIndex];
            g.addEdge(curr, nodes[curr.from], nodes[curr.to], isDirected ? EdgeType.DIRECTED : EdgeType.UNDIRECTED);

            vv.getRenderContext().getPickedVertexState().clear();
            vv.getRenderContext().getPickedEdgeState().clear();
            vv.getRenderContext().getPickedVertexState().pick(from, true);
            vv.getRenderContext().getPickedVertexState().pick(to, true);
            vv.getRenderContext().getPickedEdgeState().pick(edges[currIndex], true);
            vv.repaint();

            ++currIndex;
        }

        @Override
        public void run() {
            addNextEdge();
            if (done) {
                vv.getRenderContext().getPickedVertexState().clear();
                vv.getRenderContext().getPickedEdgeState().clear();

                edges = edgesCpy.clone();
                cancel();
            }
        }
    }


    private void clearNodesColors() {
        for (Node curr : nodes) curr.unsetVisited();
    }


    private void setListOfNodesVisited(List<Integer> nodesIndices) {
        int startVal, endVal;
        try {
            startVal = getStartComboBoxValue();
            endVal = getEndComboBoxValue();

        } catch (Exception e) {
            showErrorMessage("Coloring Nodes", e.getMessage());
            return;
        }
        for (Integer i : nodesIndices) {
            if (i == startVal || i == endVal) continue;
            nodes[i].setVisited();
        }
        refreshGraphColors();
    }


    private void refreshComboBoxes() {

        int startItem, endItem;
        try {
            startItem = getStartComboBoxValue();
        } catch (Exception e) {
            startItem = -1;
        }
        try {
            endItem = getEndComboBoxValue();
        } catch (Exception e) {
            endItem = -1;
        }

        endVertexCbx.removeAllItems();
        startVertexCbx.removeAllItems();

        for (int i = 0; i < nodeFreq.length; ++i) {
            if (nodeFreq[i] > 0) {
                endVertexCbx.addItem(String.valueOf(i));
                startVertexCbx.addItem(String.valueOf(i));
            }
        }
        startVertexCbx.setSelectedItem(String.valueOf(startItem));
        endVertexCbx.setSelectedItem(String.valueOf(endItem));
    }


    // ---------------------------------------------------------------
    private void initTable() {
        edgesTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        DefaultTableModel tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }

        };
        edgesTable.setModel(tableModel);

        // init rows
        for (int i = 0; i < Constants.MAX_EDGES; ++i) {
            tableModel.addRow(new Integer[columns.length]);
            tableModel.setValueAt(tableModel.getRowCount(), tableModel.getRowCount() - 1, 0);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        edgesTable.getColumn(columns[0]).setCellRenderer(centerRenderer);
        edgesTable.getColumn(columns[1]).setCellRenderer(centerRenderer);
        edgesTable.getColumn(columns[2]).setCellRenderer(centerRenderer);
        edgesTable.getColumn(columns[3]).setCellRenderer(centerRenderer);

        tableModel.addTableModelListener(this::tableEditChanged);


    }

    private void tableEditChanged(TableModelEvent event) {

        int selectedRow = edgesTable.getSelectedRow();
        if (selectedRow < 0) return;
        Edge edge = edges[selectedRow];
        if (!isCompleteRow(selectedRow)) {
            removeEdgeFromGraph(edge, selectedRow);
            refreshGraph();
            return;
        }

        Node from = nodes[(Integer.parseInt((String) edgesTable.getValueAt(selectedRow, 1)))];
        Node to = nodes[(Integer.parseInt((String) edgesTable.getValueAt(selectedRow, 2)))];
        int cost = (Integer.parseInt((String) edgesTable.getValueAt(selectedRow, 3)));

        // old edge
        if (edge != null) removeEdgeFromGraph(edge, selectedRow);
        edges[selectedRow] = new Edge(from, to, cost);

        refreshGraph();

    }

    private boolean isCompleteRow(int row) {
        return (checkValue(row, 1, true) && checkValue(row, 2, true) && checkValue(row, 3, false));

    }

    private boolean checkValue(int row, int col, boolean checkNode) {
        if (row >= edgesTable.getRowCount() || col >= edgesTable.getColumnCount()) return false;

        Object value = edgesTable.getValueAt(row, col);
        int val;
        if (value == null || value.toString().isEmpty())
            return false;
        try {
            val = Integer.parseInt(value.toString());
        } catch (Exception e) {
            showErrorMessage("Edges table datatype", "Unsupported datatype in (" + row + "," + col + ") please enter integers only");
            edgesTable.setValueAt(null, row, col);
            return false;
        }
        if (val < 0) {
            showErrorMessage("Edges table value", "Negative values are not accepted.");
            edgesTable.setValueAt(null, row, col);
            return false;
        }
        if (checkNode) {
            try {
                Node node = nodes[val];
                if (node == null) throw new Exception();
            } catch (Exception e) {
                showErrorMessage("Edges table datatype", "Node out of bound in (" + row + "," + col + ") please enter value between 0 and " + Constants.MAX_VERTICES + " only");
                edgesTable.setValueAt(null, row, col);
                return false;
            }
        }

        return true;
    }


}
