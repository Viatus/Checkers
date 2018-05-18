package game;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс поля игры
 */
public class Field {

    private final int FIELD_WIDTH = 8;

    private Checker turn = Checker.white;

    private Checker winner = Checker.empty;

    private Map<Cell, Chip> checkersField = new HashMap<Cell, Chip>();

    public Field() {
        for (int i = 0; i < FIELD_WIDTH / 2 - 1; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        checkersField.put(new Cell(i, j), new Chip(Checker.white));
                        checkersField.put(new Cell(FIELD_WIDTH - 1 - i, j), new Chip(Checker.empty));
                    } else {
                        checkersField.put(new Cell(i, j), new Chip(Checker.empty));
                        checkersField.put(new Cell(FIELD_WIDTH - 1 - i, j), new Chip(Checker.black));
                    }
                } else {
                    if (j % 2 == 1) {
                        checkersField.put(new Cell(i, j), new Chip(Checker.white));
                        checkersField.put(new Cell(FIELD_WIDTH - 1 - i, j), new Chip(Checker.empty));
                    } else {
                        checkersField.put(new Cell(i, j), new Chip(Checker.empty));
                        checkersField.put(new Cell(FIELD_WIDTH - 1 - i, j), new Chip(Checker.black));
                    }
                }
            }
        }
        for (int i = FIELD_WIDTH / 2 - 1; i < FIELD_WIDTH / 2 + 1; i++) {
            for (int j = 0; j < 8; j++) {
                checkersField.put(new Cell(i, j), new Chip(Checker.empty));
            }
        }
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

    public void makeTurn(Cell startCell, Cell endCell) {
        if (Math.abs(startCell.getX() - endCell.getX()) != Math.abs(startCell.getY() - endCell.getY())
                || (checkersField.get(startCell).getChecker() == Checker.empty)) {
            return;
        }
        Chip movingChip = checkersField.get(startCell);
        Chip destinationChip = checkersField.get(endCell);
        if (!movingChip.getIsKing()) {
            switch (Math.abs(startCell.getX() - endCell.getX()) + Math.abs(startCell.getY() - endCell.getY())) {
                case 2:
                    if (destinationChip.getChecker() == Checker.empty) {
                        if ((startCell.getX() - endCell.getX() > 0 && movingChip.getChecker() == Checker.black)
                                || (startCell.getX() - endCell.getX() < 0 && movingChip.getChecker() == Checker.white))
                            moveChip(startCell, endCell);
                    }
                    break;
                case 4:
                    if (checkersField.get(new Cell(endCell.getX() - (endCell.getX() - startCell.getX()) / 2, endCell.getY() - (endCell.getY() - startCell.getY()) / 2)).getChecker()
                            == movingChip.getChecker().flip()
                            && destinationChip.getChecker() == Checker.empty) {
                        eatChip(startCell, endCell);
                    }
                    break;
            }
        } else {
            int chipSequence = 0;
            int chipsInTotal = 0;
            if (checkersField.get(endCell).getChecker() == Checker.empty) {
                for (int i = Math.min(startCell.getX(), endCell.getX()) + 1; i < Math.max(startCell.getX(), endCell.getX()); i++) {
                    for (int j = Math.min(startCell.getY(), endCell.getY()) + 1; j < Math.max(startCell.getY(), endCell.getY()); j++) {
                        if (checkersField.get(new Cell(i, j)).getChecker() != Checker.empty) {
                            chipSequence++;
                            chipsInTotal++;
                        } else {
                            chipSequence = 0;
                        }
                        if (chipSequence > 1 || checkersField.get(new Cell(i, j)).getChecker() == movingChip.getChecker()) {
                            return;
                        }
                    }
                }
                if (chipsInTotal == 0) {
                    moveChip(startCell, endCell);
                } else {
                    eatChip(startCell, endCell);
                }
            }
        }
    }


    private void moveChip(Cell startCell, Cell endCell) {
        checkersField.put(endCell, checkersField.get(startCell));
        checkersField.put(startCell, new Chip(Checker.empty));
        becomeKing(endCell);
        changeTurn();
    }

    private void eatChip(Cell startCell, Cell endCell) {
        Chip currentChip = checkersField.get(startCell);
        if (!currentChip.getIsKing()) {
            checkersField.put(startCell, new Chip(Checker.empty));
            checkersField.put(endCell, currentChip);
            checkersField.put(new Cell(endCell.getX() - (endCell.getX() - startCell.getX()) / 2, endCell.getY() - (endCell.getY() - startCell.getY()) / 2), new Chip(Checker.empty));
            becomeKing(endCell);
        } else {
            checkersField.put(startCell, new Chip(Checker.empty));
            checkersField.put(endCell, currentChip);
            for (int i = Math.min(startCell.getX(), endCell.getX()) + 1; i < Math.max(startCell.getX(), endCell.getX()); i++) {
                for (int j = Math.min(startCell.getY(), endCell.getY()) + 1; j < Math.max(startCell.getY(), endCell.getY()); j++) {
                    checkersField.put(new Cell(i, j), new Chip(Checker.empty));
                }
            }
        }
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
    }

    private void checkVictor() {
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                Cell currentCell = new Cell(i, j);
                Chip currentChip = checkersField.get(currentCell);
                if (currentChip.getChecker() == turn.flip()) {
                    if (isAnyMoreEats(currentCell)) {
                        return;
                    }
                    if (currentChip.getChecker() == Checker.white) {
                        if (currentCell.getX() + 1 <= 7 && currentCell.getY() + 1 <= 7) {
                            if (checkersField.get(new Cell(currentCell.getX() + 1, currentCell.getY() + 1)).getChecker() == Checker.empty) {
                                return;
                            }
                        }
                        if (currentCell.getX() + 1 <= 7 && currentCell.getY() - 1 >= 0) {
                            if (checkersField.get(new Cell(currentCell.getX() + 1, currentCell.getY() - 1)).getChecker() == Checker.empty) {
                                return;
                            }
                        }
                        if (currentChip.getIsKing() && currentCell.getX() - 1 >= 0 && currentCell.getY() + 1 <= 7) {
                            if (checkersField.get(new Cell(currentCell.getX() - 1, currentCell.getY() + 1)).getChecker() == Checker.empty) {
                                return;
                            }
                        }
                        if (currentChip.getIsKing() && currentCell.getX() - 1 >= 0 && currentCell.getY() - 1 >= 0) {
                            if (checkersField.get(new Cell(currentCell.getX() - 1, currentCell.getY() - 1)).getChecker() == Checker.empty) {
                                return;
                            }
                        }
                    }
                    if (currentChip.getChecker() == Checker.black) {
                        if (currentCell.getX() - 1 >= 0 && currentCell.getY() + 1 <= 7) {
                            if (checkersField.get(new Cell(currentCell.getX() - 1, currentCell.getY() + 1)).getChecker() == Checker.empty) {
                                return;
                            }
                        }
                        if (currentCell.getX() - 1 >= 0 && currentCell.getY() - 1 >= 0) {
                            if (checkersField.get(new Cell(currentCell.getX() - 1, currentCell.getY() - 1)).getChecker() == Checker.empty) {
                                return;
                            }
                        }
                        if (currentChip.getIsKing() && currentCell.getX() + 1 <= 7 && currentCell.getY() + 1 <= 7) {
                            if (checkersField.get(new Cell(currentCell.getX() + 1, currentCell.getY() + 1)).getChecker() == Checker.empty) {
                                return;
                            }
                        }
                        if (currentChip.getIsKing() && currentCell.getX() + 1 <= 7 && currentCell.getY() - 1 >= 0) {
                            if (checkersField.get(new Cell(currentCell.getX() + 1, currentCell.getY() - 1)).getChecker() == Checker.empty) {
                                return;
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
        boolean isAnyPossibleMoves = false;
        if (!currentChip.getIsKing()) {
            if (currentCell.getX() + 2 <= 7 && currentCell.getY() + 2 <= 7) {
                if (checkersField.get(new Cell(currentCell.getX() + 1, currentCell.getY() + 1)).getChecker() == currentChip.getChecker().flip()
                        && checkersField.get(new Cell(currentCell.getX() + 2, currentCell.getY() + 2)).getChecker() == Checker.empty) {
                    isAnyPossibleMoves = true;
                }
            }
            if (currentCell.getX() + 2 <= 7 && currentCell.getY() - 2 >= 0) {
                if (checkersField.get(new Cell(currentCell.getX() + 1, currentCell.getY() - 1)).getChecker() == currentChip.getChecker().flip()
                        && checkersField.get(new Cell(currentCell.getX() + 2, currentCell.getY() - 2)).getChecker() == Checker.empty) {
                    isAnyPossibleMoves = true;
                }
            }
            if (currentCell.getX() - 2 >= 0 && currentCell.getY() + 2 <= 7) {
                if (checkersField.get(new Cell(currentCell.getX() - 1, currentCell.getY() + 1)).getChecker() == currentChip.getChecker().flip()
                        && checkersField.get(new Cell(currentCell.getX() - 2, currentCell.getY() + 2)).getChecker() == Checker.empty) {
                    isAnyPossibleMoves = true;
                }
            }
            if (currentCell.getX() - 2 >= 0 && currentCell.getY() - 2 >= 0) {
                if (checkersField.get(new Cell(currentCell.getX() - 1, currentCell.getY() - 1)).getChecker() == currentChip.getChecker().flip()
                        && checkersField.get(new Cell(currentCell.getX() - 2, currentCell.getY() - 2)).getChecker() == Checker.empty) {
                    isAnyPossibleMoves = true;
                }
            }
        } else {
            for (int i = 1; i <= FIELD_WIDTH - 1; i++) {
                if (currentCell.getX() + i + 1 <= 7 && currentCell.getY() + i + 1 <= 7) {
                    if (checkersField.get(new Cell(currentCell.getX() + i, currentCell.getY() + i)).getChecker() == currentChip.getChecker().flip()
                            && checkersField.get(new Cell(currentCell.getX() + i + 1, currentCell.getY() + i + 1)).getChecker() == Checker.empty) {
                        isAnyPossibleMoves = true;
                    }
                }
                if (currentCell.getX() + i + 1 <= 7 && currentCell.getY() - i - 1 >= 0) {
                    if (checkersField.get(new Cell(currentCell.getX() + i, currentCell.getY() - i)).getChecker() == currentChip.getChecker().flip()
                            && checkersField.get(new Cell(currentCell.getX() + i + 1, currentCell.getY() - i - 1)).getChecker() == Checker.empty) {
                        isAnyPossibleMoves = true;
                    }
                }
                if (currentCell.getX() - i - 1 >= 0 && currentCell.getY() + i + 1 <= 7) {
                    if (checkersField.get(new Cell(currentCell.getX() - i, currentCell.getY() + i)).getChecker() == currentChip.getChecker().flip()
                            && checkersField.get(new Cell(currentCell.getX() - i - 1, currentCell.getY() + i + 1)).getChecker() == Checker.empty) {
                        isAnyPossibleMoves = true;
                    }
                }
                if (currentCell.getX() - i - 1 >= 0 && currentCell.getY() - i - 1 >= 0) {
                    if (checkersField.get(new Cell(currentCell.getX() - i, currentCell.getY() - i)).getChecker() == currentChip.getChecker().flip()
                            && checkersField.get(new Cell(currentCell.getX() - i - 1, currentCell.getY() - i - 1)).getChecker() == Checker.empty) {
                        isAnyPossibleMoves = true;
                    }
                }
            }
        }
        return isAnyPossibleMoves;
    }

    private void becomeKing(Cell currentCell) {
        Chip currentChip = checkersField.get(currentCell);
        if ((currentChip.getChecker() == Checker.white && currentCell.getX() == 7) || (currentChip.getChecker() == Checker.black && currentCell.getX() == 0)) {
            checkersField.get(currentCell).makeKing();
        }
    }
}
