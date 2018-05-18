package gui;

import game.Cell;
import game.Checker;
import game.Chip;
import game.Field;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CheckersPanel extends JPanel {
    private static final int CELL_SIZE = 100;

    private static final int CHECKER_RAD = 60;

    private Field field;

    private boolean isGameRunning;

    private Cell selectedCell;

    private void initListener() {
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onPress(e);
            }
        };
        addMouseListener(mouseListener);
    }

    private void onPress(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && isGameRunning) {
            if (7 - e.getY() / 100 >= 0 && 7 - e.getY() / 100 <= 7 && e.getX() / 100 >= 0 && e.getX() / 100 <= 7) {
                selectedCellChanged(new Cell(7 - e.getY() / 100, e.getX() / 100));
            }
        }
        repaint();
        if (field.getWinner() != Checker.empty && isGameRunning) {
            new JOptionPane().showMessageDialog(null, "Победил " + field.getWinner().toString(), "Игра завершена", JOptionPane.INFORMATION_MESSAGE);
            isGameRunning = false;
        }
    }


    public void selectedCellChanged(Cell possibleCell) {
        Chip possibleChip = field.get(possibleCell);
        if (possibleChip.getChecker() == field.getTurn()) {
            selectedCell = possibleCell;
        } else {
            if (!selectedCell.equals(new Cell(9, 9))) {
                if (field.get(selectedCell).getChecker() == field.getTurn()) {
                    Checker previousTurn = field.getTurn();
                    field.makeTurn(selectedCell, possibleCell);
                    if (previousTurn != field.getTurn()) {
                        selectedCell = new Cell(9, 9);
                    } else {
                        selectedCell = possibleCell;
                    }
                }
            }
        }
    }

    public CheckersPanel() {
        super();
        field = new Field();
        selectedCell = new Cell(9, 9);
        isGameRunning = true;
        initListener();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon lightCell = new ImageIcon("./src/main/resources/Light_cell.jpg");
        ImageIcon darkCell = new ImageIcon("./src/main/resources/Dark_cell.jpg");
        ImageIcon blackChecker = new ImageIcon("./src/main/resources/Black_checker.png");
        ImageIcon blackKing = new ImageIcon("./src/main/resources/Black_king.png");
        ImageIcon whiteKing = new ImageIcon("./src/main/resources/White_king.png");
        ImageIcon whiteChecker = new ImageIcon("./src/main/resources/White_checker.png");
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if ((i + j) % 2 == 0) {
                    g2d.drawImage(darkCell.getImage(), CELL_SIZE * j, CELL_SIZE * (7 - i), this);
                } else {
                    g2d.drawImage(lightCell.getImage(), CELL_SIZE * j, CELL_SIZE * (7 - i), this);
                }
                switch (field.get(new Cell(i, j)).getChecker()) {
                    case white:
                        if (field.get(new Cell(i, j)).getIsKing()) {
                            g2d.drawImage(whiteKing.getImage(),CELL_SIZE * j + 20,CELL_SIZE * (7 - i) + 20,60,60,this);
                        } else {
                            g2d.drawImage(whiteChecker.getImage(),CELL_SIZE * j + 20,CELL_SIZE * (7 - i) + 20,60,60,this);
                        }
                        break;
                    case black:
                        if (field.get(new Cell(i, j)).getIsKing()) {
                            g2d.drawImage(blackKing.getImage(),CELL_SIZE * j + 20,CELL_SIZE * (7 - i) + 20,60,60,this);
                        } else {
                            g2d.drawImage(blackChecker.getImage(),CELL_SIZE * j + 20,CELL_SIZE * (7 - i) + 20,60,60,this);
                        }
                        break;
                }
            }
        }
    }

}
