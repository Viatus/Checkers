package gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CheckersFrame extends JFrame {

    private CheckersPanel checkersPanel;


    private void initMainPanel() {
        checkersPanel = new CheckersPanel();
        checkersPanel.setPreferredSize(new Dimension(955, 830));
        checkersPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

    }

    private void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                onQuit();
            }
        });


    }

    private void onQuit() {
        String[] vars = {"Да", "Нет"};
        int result = JOptionPane.showOptionDialog(this, "Действительно выйти?",
                "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, vars, "Да");
        if (result == JOptionPane.YES_OPTION)
            System.exit(0);
    }

    public CheckersFrame(String s) {
        super(s);
        setSize(975, 850);
        this.setLayout(new BorderLayout());
        initMainPanel();
        setVisible(true);
        initListeners();
        this.add(checkersPanel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMaximumSize(getSize());
        setMinimumSize(getSize());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckersFrame("Русские шашки"));
    }
}
