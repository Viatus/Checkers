package game;

import com.sun.corba.se.spi.ior.IdentifiableContainerBase;
import javafx.util.Pair;

import java.util.*;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.StrictMath.abs;

/**
 * Класс поля игры
 */
public class Field {

    private final int FIELD_WIDTH = 8;

    private Checker turn = Checker.white;

    private Checker winner = Checker.empty;

    private int amountOfWhite;
    private int amountOfBlack;

    private boolean isPreviousEat;

    private Map<Cell, Chip> checkersField = new HashMap<Cell, Chip>();

    private void putCheckers(int i, int j, int d) {
        if (j % 2 == d) {
            checkersField.put(new Cell(i, j), new Chip(Checker.white));
            checkersField.put(new Cell(FIELD_WIDTH - 1 - i, j), new Chip(Checker.empty));
        } else {
            checkersField.put(new Cell(i, j), new Chip(Checker.empty));
            checkersField.put(new Cell(FIELD_WIDTH - 1 - i, j), new Chip(Checker.black));
        }
    }

    public Field() {
        for (int i = 0; i < FIELD_WIDTH / 2 - 1; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (i % 2 == 0) {
                    putCheckers(i, j, 0);
                } else {
                    putCheckers(i, j, 1);
                }
            }
        }
        for (int i = FIELD_WIDTH / 2 - 1; i < FIELD_WIDTH / 2 + 1; i++) {
            for (int j = 0; j < 8; j++) {
                checkersField.put(new Cell(i, j), new Chip(Checker.empty));
            }
        }
        amountOfBlack = 12;
        amountOfWhite = 12;
        isPreviousEat = false;
    }

    public Chip get(int x, int y) {
        return get(new Cell(x, y));
    }

    public Chip get(Cell cell) {
        return checkersField.get(cell);
    }

    public Checker getTurn() {
        return turn;
    }

    public Checker getWinner() {
        return winner;
    }

    public int getAmountOfBlack() {
        return amountOfBlack;
    }

    public int getAmountOfWhite() {
        return amountOfWhite;
    }

    public boolean makeTurn(Cell startCell, Cell endCell) {
        if (abs(startCell.getX() - endCell.getX()) != abs(startCell.getY() - endCell.getY())
                || (checkersField.get(startCell).getChecker() == Checker.empty)) {
            return false;
        }
        Chip movingChip = checkersField.get(startCell);
        Chip destinationChip = checkersField.get(endCell);
        if (!movingChip.getIsKing()) {
            switch (abs(startCell.getX() - endCell.getX()) + abs(startCell.getY() - endCell.getY())) {
                case 2:
                    if (destinationChip.getChecker() == Checker.empty && !isPreviousEat) {
                        if ((startCell.getX() - endCell.getX() > 0 && movingChip.getChecker() == Checker.black)
                                || (startCell.getX() - endCell.getX() < 0 && movingChip.getChecker() == Checker.white))
                            moveChip(startCell, endCell);
                    } else {
                        return false;
                    }
                    break;
                case 4:
                    if (get(endCell.getX() - (endCell.getX() - startCell.getX()) / 2, endCell.getY() - (endCell.getY() - startCell.getY()) / 2).getChecker()
                            == movingChip.getChecker().flip()
                            && destinationChip.getChecker() == Checker.empty) {
                        eatChip(startCell, endCell);
                    } else {
                        return false;
                    }
                    break;
            }
        } else {
            int chipSequence = 0;
            int chipsInTotal = 0;
            if (checkersField.get(endCell).getChecker() == Checker.empty) {
                for (int i = 1; i < abs(startCell.getX() - endCell.getX()); i++) {
                    if (get(min(startCell.getX(), endCell.getX()) + i, min(startCell.getY(), endCell.getY()) + i).getChecker() != Checker.empty) {
                        chipSequence++;
                        chipsInTotal++;
                    } else {
                        chipSequence = 0;
                    }
                    if (chipSequence > 1 || get(min(startCell.getX(), endCell.getX()) + i, min(startCell.getY(), endCell.getY()) + i).getChecker() == movingChip.getChecker()) {
                        return false;
                    }
                }
                if (chipsInTotal == 0) {
                    if (!isPreviousEat) {
                        moveChip(startCell, endCell);
                    }
                } else {
                    eatChip(startCell, endCell);
                }
            } else {
                return false;
            }
        }
        return true;
    }


    private void moveChip(Cell startCell, Cell endCell) {
        checkersField.put(endCell, checkersField.get(startCell));
        checkersField.put(startCell, new Chip(Checker.empty));
        becomeKing(endCell);
        changeTurn();
    }

    private void eatChip(Cell startCell, Cell endCell) {
        switch (turn) {
            case white:
                amountOfBlack--;
                break;
            case black:
                amountOfWhite--;
                break;
        }
        Chip currentChip = checkersField.get(startCell);
        if (!currentChip.getIsKing()) {
            checkersField.put(startCell, new Chip(Checker.empty));
            checkersField.put(endCell, currentChip);
            checkersField.put(new Cell(endCell.getX() - (endCell.getX() - startCell.getX()) / 2, endCell.getY() - (endCell.getY() - startCell.getY()) / 2), new Chip(Checker.empty));
            becomeKing(endCell);
        } else {
            checkersField.put(startCell, new Chip(Checker.empty));
            checkersField.put(endCell, currentChip);
            for (int i = min(startCell.getX(), endCell.getX()) + 1; i < max(startCell.getX(), endCell.getX()); i++) {
                for (int j = min(startCell.getY(), endCell.getY()) + 1; j < max(startCell.getY(), endCell.getY()); j++) {
                    checkersField.put(new Cell(i, j), new Chip(Checker.empty));
                }
            }
        }
        isPreviousEat = true;
        if (!isAnyMoreEats(endCell)) {
            changeTurn();
        }
    }

    private void changeTurn() {
        checkVictor();
        if (turn == Checker.white) {
            turn = Checker.black;
        } else {
            turn = Checker.white;
        }
        isPreviousEat = false;
    }

    private void checkVictor() {
        List<Pair<Integer, Integer>> directions = Arrays.asList(new Pair<>(1, 1), new Pair<>(1, -1), new Pair<>(-1, 1), new Pair<>(-1, -1));
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                Cell currentCell = new Cell(i, j);
                Chip currentChip = checkersField.get(currentCell);
                if (currentChip.getChecker() == turn.flip()) {
                    if (isAnyMoreEats(currentCell)) {
                        return;
                    }
                    for (Pair<Integer, Integer> pair : directions) {
                        if (pair.getKey() > 0 && (currentChip.getIsKing() || currentChip.getChecker() == Checker.white)) {
                            if (currentCell.getX() + pair.getKey() <= 7 && currentCell.getX() + pair.getKey() >= 0 && currentCell.getY() + pair.getValue() >= 0 && currentCell.getY() + pair.getValue() <= 7) {
                                if (get(currentCell.getX() + pair.getKey(), currentCell.getY() + pair.getValue()).getChecker() == Checker.empty) {
                                    return;
                                }
                            }
                        } else {
                            if (currentChip.getIsKing() || currentChip.getChecker() == Checker.black) {
                                if (currentCell.getX() + pair.getKey() <= 7 && currentCell.getX() + pair.getKey() >= 0 && currentCell.getY() + pair.getValue() >= 0 && currentCell.getY() + pair.getValue() <= 7) {
                                    if (get(currentCell.getX() + pair.getKey(), currentCell.getY() + pair.getValue()).getChecker() == Checker.empty) {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        winner = turn;
    }

    private boolean isAnyMoreEats(Cell currentCell) {
        Chip currentChip = checkersField.get(currentCell);
        List<Pair<Integer, Integer>> directions = Arrays.asList(new Pair<>(1, 1), new Pair<>(1, -1), new Pair<>(-1, 1), new Pair<>(-1, -1));
        if (!currentChip.getIsKing()) {
            for (Pair<Integer, Integer> pair : directions) {
                if (currentCell.getX() + 2 * pair.getKey() <= 7 && currentCell.getX() + 2 * pair.getKey() >= 0 && currentCell.getY() + 2 * pair.getValue() >= 0 && currentCell.getY() + 2 * pair.getValue() <= 7) {
                    if (get(currentCell.getX() + pair.getKey(), currentCell.getY() + pair.getValue()).getChecker() == currentChip.getChecker().flip()
                            && get(currentCell.getX() + 2 * pair.getKey(), currentCell.getY() + 2 * pair.getValue()).getChecker() == Checker.empty) {
                        return true;
                    }
                }
            }
        } else {
            for (int i = 1; i < FIELD_WIDTH - 1; i++) {
                int checkerSequence = 0;
                for (Pair<Integer, Integer> pair : directions) {
                    if (currentCell.getX() + (i + 1) * pair.getKey() <= 7 && currentCell.getX() + (i + 1) * pair.getKey() >= 0 && currentCell.getY() + (i + 1) * pair.getValue() >= 0 && currentCell.getY() + (i + 1) * pair.getValue() <= 7) {
                        if (get(currentCell.getX() + pair.getKey(), currentCell.getY() + pair.getValue()).getChecker() == currentChip.getChecker().flip()) {
                            checkerSequence++;
                        } else {
                            checkerSequence = 0;
                        }
                        if (checkerSequence > 1 || get(currentCell.getX() + pair.getKey(), currentCell.getY() + pair.getValue()).getChecker() == currentChip.getChecker()) {
                            continue;
                        }
                        if (get(currentCell.getX() + pair.getKey(), currentCell.getY() + pair.getValue()).getChecker() == currentChip.getChecker().flip()
                                && get(currentCell.getX() + 2 * pair.getKey(), currentCell.getY() + 2 * pair.getValue()).getChecker() == Checker.empty) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void becomeKing(Cell currentCell) {
        Chip currentChip = checkersField.get(currentCell);
        if ((currentChip.getChecker() == Checker.white && currentCell.getX() == 7) || (currentChip.getChecker() == Checker.black && currentCell.getX() == 0)) {
            checkersField.get(currentCell).makeKing();
        }
    }
}
