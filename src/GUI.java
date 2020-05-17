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

@SuppressWarnings("rawtypes")
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


    Graph<Node, Edge> g;
    VisualizationViewer<Node, Edge> vv;
    Layout<Node, Edge> layout;

    private DefaultTableModel tableModel;
    Node[] nodes;
    Edge[] edges;
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

        startVertexCbx.addActionListener(e -> handleComboBoxChanges(Node.PortalType.START));
        endVertexCbx.addActionListener(e -> handleComboBoxChanges(Node.PortalType.END));

        clearAllButton.addActionListener(e -> clearAll());
//        --------------------------------------------------------
        ButtonGroup bG = new ButtonGroup();
        bG.add(directedRadio);
        bG.add(undirectedRadio);
        directedRadio.setSelected(true);

        directedRadio.addActionListener(e -> handleDirectedRadioChanges(true));
        undirectedRadio.addActionListener(e -> handleDirectedRadioChanges(false));
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
        dijkstraShortestPathButton.addActionListener(e -> {
            try {
                handleAlgorithmExecution(AlgorithmsHandler.AlgorithmType.DIJKSTRA);
            } catch (Exception ex) {
                showErrorMessage("Dijkstra", ex.getMessage());
            }
        });
        fordFulkersonMaximumFlowButton.addActionListener(e -> {
            try {
                handleAlgorithmExecution(AlgorithmsHandler.AlgorithmType.MAXIMUM_FLOW);
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorMessage("Ford-Fulkerson", ex.getMessage());
            }
        });
//        --------------------------------------------------------
        initTable();
        initNodesAndEdges();
        initGraph();

    }


    private void initNodesAndEdges() {
        nodes = new Node[Constants.MAX_VERTICES];
        nodeFreq = new int[nodes.length];
        for (int i = 0; i < nodes.length; ++i) nodes[i] = new Node(i);

        edges = new Edge[Constants.MAX_EDGES];
    }

    private void initGraph() {
        g = new SparseMultigraph<>();
        layout = new CircleLayout<>(g);

        layout.setSize(Constants.graphPnlDimension);
        vv = new VisualizationViewer<>(layout);

        Transformer<Node, Paint> vertexColor = i -> i.color;

        vv.setSize(Constants.graphPnlDimension); //Sets the viewing area size
        vv.getRenderContext().setVertexLabelTransformer(String::valueOf);
        vv.getRenderContext().setEdgeLabelTransformer(s -> String.valueOf(s.cost));
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);

        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<Integer, Number>();
        vv.setGraphMouse(graphMouse);
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);

        JPanel view = (JPanel) graphPnl.getViewport().getView();
        view.add(panel);
        view.validate();
        //vv.getPickedVertexState().isPicked(); // you will need it
    }

    private void clearAll() {
        for (int i = 0; i < edges.length; ++i) {
            removeEdgeFromGraph(edges[i], i);
        }
        for (int i = 0; i < edgesTable.getRowCount(); ++i) {
            edgesTable.setValueAt(null, i, 1);
            edgesTable.setValueAt(null, i, 2);
            edgesTable.setValueAt(null, i, 3);
        }
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
        try {
            return Integer.parseInt(startItem);
        } catch (Exception e) {
            throw new Exception("Unsupported datatype in start vertex");
        }
    }

    private int getEndComboBoxValue() throws Exception {
        if (endVertexCbx.getSelectedItem() == null) throw new Exception("No value is selected in end vertex.");
        String startItem = String.valueOf(endVertexCbx.getSelectedItem());
        try {
            return Integer.parseInt(startItem);
        } catch (Exception e) {
            throw new Exception("Unsupported datatype in end vertex");
        }
    }

    private void handleComboBoxChanges(Node.PortalType type) {
        int startVal, endVal;

        try {
            startVal = getStartComboBoxValue();
            endVal = getEndComboBoxValue();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        clearNodesColors();
        nodes[startVal].setPortal(Node.PortalType.START);
        nodes[endVal].setPortal(Node.PortalType.END);
        refreshGraphColors();


    }


    private void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, "Error in " + title, JOptionPane.ERROR_MESSAGE);
    }

    private void handleAlgorithmExecution(AlgorithmsHandler.AlgorithmType algorithmType) throws Exception {
        startVertexCbx.setSelectedIndex(startVertexCbx.getSelectedIndex()); // refresh colors from previous execution
        List<Edge> edges = null;// TODO: fill here edges
        Edge.GraphType graphType = isDirected ? Edge.GraphType.DIRECTED : Edge.GraphType.UNDIRECTED;
        Solution solution;

        try {
            AlgorithmsHandler.executeAlgorithm(edges, nodes[getStartComboBoxValue()], nodes[getEndComboBoxValue()],graphType,algorithmType );
        } catch (Exception e) {
            showErrorMessage("Algorithm Execution", e.getMessage());
            return;
        }
        // TODO: show results
    }

    private void clearNodesColors() {
        for (Node curr : nodes) curr.unsetVisited();
    }

    private void removeEdgeFromGraph(Edge e, int idx) {
        if (e != null) {
            g.removeEdge(e);
            g.removeVertex(nodes[e.from]);
            g.removeVertex(nodes[e.to]);
        }
        edges[idx] = null;
    }

    private void refreshGraphColors() {
        vv.repaint();
    }

    private void refreshGraph() {
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

    private void refreshComboBoxes() {
        endVertexCbx.removeAllItems();
        startVertexCbx.removeAllItems();

        int siz = 0;
        for (int i = 0; i < nodeFreq.length; ++i) {
            if (nodeFreq[i] > 0) {
                endVertexCbx.addItem(String.valueOf(i));
                startVertexCbx.addItem(String.valueOf(i));
                ++siz;
            }
        }
        if (siz > 1) {
            startVertexCbx.setSelectedIndex(0);
            endVertexCbx.setSelectedIndex(1);
        }
    }


    // ---------------------------------------------------------------
    private void initTable() {
        edgesTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }

        };
        edgesTable.setModel(tableModel);

        tableModel.addColumn(columns[0]);
        tableModel.addColumn(columns[1]);
        tableModel.addColumn(columns[2]);
        tableModel.addColumn(columns[3]);

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
        if (val <= 0) {
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
