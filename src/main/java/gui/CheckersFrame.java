package gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CheckersFrame extends JFrame {

    private CheckersPanel checkersPanel;

    private InfoPanel infoPanel;

    private ActionListener quitListener;

    private void initMainPanel() {
        checkersPanel = new CheckersPanel();
        checkersPanel.setPreferredSize(new Dimension(800, 800));
        checkersPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

    }

    private void initInfoPanel() {
        infoPanel = new InfoPanel();
        infoPanel.setPreferredSize(new Dimension(200, 500));
        infoPanel.setMinimumSize(new Dimension(150, 500));
        infoPanel.setBorder(new BevelBorder(BevelBorder.RAISED));

    }

    private void initListeners() {
        quitListener = e -> onQuit();
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
        setSize(1000, 860);
        this.setLayout(new BorderLayout());
        initMainPanel();
        setVisible(true);
        JScrollPane scrollPanel = new JScrollPane(checkersPanel);
        scrollPanel.setMinimumSize(new Dimension(200, 200));
        scrollPanel.setPreferredSize(new Dimension(500, 500));
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanel, infoPanel),
                BorderLayout.CENTER);
        initListeners();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckersFrame("Русские шашки"));
    }
}
