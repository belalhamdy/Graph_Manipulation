import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Function;

@SuppressWarnings("rawtypes")
public class GUI {
    private JPanel mainPnl;
    private JRadioButton directedRadio;
    private JRadioButton undirectedRadio;
    private JComboBox endVertexCbx;
    private JComboBox startVertexCbx;
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

    public GUI() {
        JFrame form = new JFrame("Graph Manipulation");
        form.setMinimumSize(new Dimension(1100, 750));
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
        startVertexCbx.addActionListener(e -> handleComboBoxChanges(0));
        endVertexCbx.addActionListener(e -> handleComboBoxChanges(1));

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
        initNodes();
        initGraph();

    }

    private void initTable() {
        String[] columns = {
           "Id","From","To","Cost"
        };

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

        tableModel.addRow(new Integer[3]);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        edgesTable.getColumn(columns[0]).setCellRenderer(centerRenderer);
        edgesTable.getColumn(columns[1]).setCellRenderer(centerRenderer);
        edgesTable.getColumn(columns[2]).setCellRenderer(centerRenderer);
        edgesTable.getColumn(columns[3]).setCellRenderer(centerRenderer);

        tableModel.addTableModelListener(this::tableEditChanged);
    }



    private void initNodes() {
        nodes = new Node[Constants.MAX_VERTICES];
        for (int i = 0; i < nodes.length; ++i) nodes[i] = new Node(i);
    }

    private void initGraph() {
        g = new SparseMultigraph<>();
        layout = new CircleLayout<>(g);

        layout.setSize(Constants.graphPnlDimension);
        vv = new VisualizationViewer<>(layout);

        vv.setSize(Constants.graphPnlDimension); //Sets the viewing area size
        vv.getRenderContext().setVertexLabelTransformer(String::valueOf);
        vv.getRenderContext().setEdgeLabelTransformer(s -> String.valueOf(s.cost));

        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<Integer, Number>();
        vv.setGraphMouse(graphMouse);
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);

        JPanel view = (JPanel) graphPnl.getViewport().getView();
        view.add(panel);
        view.validate();

        //vv.getPickedVertexState().isPicked(); // you will need it
    }

    private void clearAll(){
        // TODO: clear all data
    }
    private void handleDirectedRadioChanges(boolean isDirected){
        showErrorMessage("h","h");
    }
    private void handleComboBoxChanges(int type){
        // StartCbx -> type = 0
        // EndCbx -> type = 1
        if(type == 0){
            // TODO: handle change of start cbx
        }
        else if (type == 1){
            // TODO: handle change of end cbx
        }
    }
    private void tableEditChanged(TableModelEvent tableModelEvent) {
        // TODO: handle change of data here
    }
    private void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, "Error in " + title, JOptionPane.ERROR_MESSAGE);
    }

    private void handleAlgorithmExecution(AlgorithmsHandler.AlgorithmType algorithmType) throws Exception {


    }

    private void refreshGraph() {
        layout = new CircleLayout<>(g);
        vv.setGraphLayout(layout);
        vv.repaint();
    }

    public class MyNodeFillPaintFunction<Node> implements Function<Node, Paint> {
        @Override
        public Paint apply(Node n) {
            return null;
        }
    }


}
