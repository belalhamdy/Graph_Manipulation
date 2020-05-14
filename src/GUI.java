import javax.swing.*;
import java.awt.*;

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

    public GUI() {
        JFrame form = new JFrame("Graph Manipulation");
        form.setMaximumSize(new Dimension(700, 800));
        form.setPreferredSize(new Dimension(600, 500));
        form.setResizable(true);
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

    }
}
