import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreditsSplashScreen {
    private JPanel panel1;
    private JButton startButton;
    private JTextArea textArea;
    private final String creditsText = "Graph Manipulation Program." +
            "\nThis Program is created by Faculty of Computers and Artificial Intelligence Cairo University Students." +
            "\nThe application manipulates different types of graphs like directed and undirected ones." +
            "\nAlso it supports Dijkstra and Maximum Flow Algorithms."+
            "\n--------------------------------------------" +
            "\nContributors:" +
            "\nBelal Hamdy Ezzat" +
            "\nAhmed Nasr El Dardery" +
            "\nSara Samer" +
            "\nSalma Salama" +
            "\n--------------------------------------------" +
            "\nPublished in 18th of June 2020.";
    JFrame form;
    CreditsSplashScreen(){
        form = new JFrame("Credits");
        form.setMinimumSize(new Dimension(300,300));
        form.setResizable(false);
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setContentPane(panel1);
        form.pack();
        form.setVisible(true);
        form.setLocationRelativeTo(null);
//        --------------------------------------------------------
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
//        --------------------------------------------------------
        startButton.addActionListener(e -> {
            new GUI();
            form.dispose();
        });
//        --------------------------------------------------------
        textArea.setText(creditsText);
    }
}
