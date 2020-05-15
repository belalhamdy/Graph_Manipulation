import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public GUI() {
        JFrame form = new JFrame("Graph Manipulation");
        form.setMinimumSize(new Dimension(1100, 700));
        form.setResizable(false);
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setContentPane(mainPnl);
        form.pack();
        form.setVisible(true);
        form.setLocationRelativeTo(null);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
//        --------------------------------------------------------
        ButtonGroup bG = new ButtonGroup();
        bG.add(directedRadio);
        bG.add(undirectedRadio);
        directedRadio.setSelected(true);
//        --------------------------------------------------------
        graphPnl.setPreferredSize(new Dimension(200,-1));
        graphPnl.setMaximumSize(new Dimension(200, -1));
        graphPnl.setMaximumSize(new Dimension(200, -1));
//        --------------------------------------------------------
        dijkstraShortestPathButton.addActionListener(e -> {
            try {
                handleAlgorithmExecution(AlgorithmsHandler.AlgorithmType.DIJKSTRA);
            } catch (Exception ex) {
                showErrorMessage("Dijkstra",ex.getMessage());
            }
        });
        fordFulkersonMaximumFlowButton.addActionListener(e -> {
            try {
                handleAlgorithmExecution(AlgorithmsHandler.AlgorithmType.MAXIMUM_FLOW);
            } catch (Exception ex) {
                showErrorMessage("Ford-Fulkerson",ex.getMessage());
            }
        });
//        --------------------------------------------------------

    }
    private void showErrorMessage(String title,String message){
        JOptionPane.showMessageDialog(null, message, "Error in " + title, JOptionPane.ERROR_MESSAGE);
    }
    private void handleAlgorithmExecution(AlgorithmsHandler.AlgorithmType algorithmType) throws Exception{
    }
}
