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
    private static final int BOARD_X = 160;
    private static final int CHECKER_SIZE = 60;

    private Field field;

    private boolean isGameRunning;

    private Cell selectedCell;

    public Field getField() {
        return field;
    }

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
            if (7 - e.getY() / 100 >= 0 && 7 - e.getY() / 100 <= 7 && (e.getX() - 160) / 100 >= 0 && (e.getX() - 160) / 100 <= 7) {
                selectedCellChanged(new Cell(7 - e.getY() / 100, (e.getX() - 160) / 100));
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
                    if (!field.makeTurn(selectedCell, possibleCell)) {
                        new JOptionPane().showMessageDialog(null, "Выбранная клетка недостижима для выбранной шашки", "Ход невозможен", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        if (previousTurn != field.getTurn()) {
                            selectedCell = new Cell(9, 9);

                        } else {
                            selectedCell = possibleCell;
                        }
                    }
                }
            } else {
                new JOptionPane().showMessageDialog(null, "Не выбрана шашка, которая должна ходить", "Ход невозможен", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public CheckersPanel() {
        super();
        field = new Field();
        selectedCell = new Cell(9, 9);
        isGameRunning = true;
        initListener();
        setLayout(new BorderLayout());
        add(new JLabel("        Сейчас ходит :"), BorderLayout.NORTH);
        add(new JLabel("     Съеденные шашки :"), BorderLayout.CENTER);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.lightGray);
        g.fillRect(0,0,this.getWidth(),getHeight());
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                ImageIcon background = (i + j) % 2 == 0 ?
                        new ImageIcon("./src/main/resources/Dark_cell.jpg") :
                        new ImageIcon("./src/main/resources/Light_cell.jpg");
                g2d.drawImage(background.getImage(), BOARD_X + CELL_SIZE * j, CELL_SIZE * (7 - i), this);
                Chip currentChip = field.get(i, j);
                ImageIcon currentIcon = currentChip.getChecker() == Checker.white ?
                        currentChip.getIsKing() ?
                                new ImageIcon("./src/main/resources/White_king.png") :
                                new ImageIcon("./src/main/resources/White_checker.png") :
                        currentChip.getChecker() == Checker.black ?
                                currentChip.getIsKing() ?
                                        new ImageIcon("./src/main/resources/Black_king.png") :
                                        new ImageIcon("./src/main/resources/Black_checker.png") :
                                null;
                if (currentIcon != null) {
                    g2d.drawImage(currentIcon.getImage(), BOARD_X + CELL_SIZE * j + CELL_SIZE / 5, CELL_SIZE * (7 - i) + (CELL_SIZE - CHECKER_SIZE) / 2, CHECKER_SIZE, CHECKER_SIZE, this);
                }
            }
        }
        switch (field.getTurn()) {
            case white:
                g2d.drawImage(new ImageIcon("./src/main/resources/White_checker.png").getImage(), 25, 40, CHECKER_SIZE *3/2, CHECKER_SIZE*3 / 2, this);
                break;
            case black:
                g2d.drawImage(new ImageIcon("./src/main/resources/Black_checker.png").getImage(), 25, 40, CHECKER_SIZE *3/2, CHECKER_SIZE*3 / 2, this);
                break;
        }
        for (int i = 0; i < 12 - field.getAmountOfWhite(); i++) {
            g2d.drawImage(new ImageIcon("./src/main/resources/White_checker.png").getImage(), 25, 440 + i * CHECKER_SIZE / 3, CHECKER_SIZE / 2, CHECKER_SIZE / 2, this);
        }
        for (int i = 0; i < 12 - field.getAmountOfBlack(); i++) {
            g2d.drawImage(new ImageIcon("./src/main/resources/Black_checker.png").getImage(), 90, 440 + i * CHECKER_SIZE / 3, CHECKER_SIZE / 2, CHECKER_SIZE / 2, this);
        }
    }

}
