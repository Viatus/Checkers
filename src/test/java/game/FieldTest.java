package game;

import org.junit.Test;

import static org.junit.Assert.*;

public class FieldTest {
    @Test
    public void makeTurn() {
        //Проверка хода обычной шашки
        Field field = new Field();
        assertFalse(field.makeTurn(new Cell(1, 0), new Cell(2, 1)));
        assertFalse(field.makeTurn(new Cell(1, 2), new Cell(2, 2)));
        assertFalse(field.makeTurn(new Cell(0, 0), new Cell(1, 1)));
        assertFalse(field.makeTurn(new Cell(0, 0), new Cell(2, 2)));

        assertTrue(field.makeTurn(new Cell(2, 0), new Cell(3, 1))
                && field.get(2, 0).getChecker() == Checker.empty && field.get(3, 1).getChecker() == Checker.white);
        field.makeTurn(new Cell(2, 2), new Cell(3, 3));
        field.makeTurn(new Cell(5, 5), new Cell(4, 4));
        assertTrue(field.makeTurn(new Cell(3, 3), new Cell(5, 5)));

        //Проверка хода дамки
        field = new Field();
        field.get(5, 3).makeKing();
        assertTrue(field.makeTurn(new Cell(2, 0), new Cell(3, 1)));
        assertTrue(field.makeTurn(new Cell(5, 3), new Cell(2, 0)));
        assertTrue(field.get(3, 1).getChecker() == Checker.empty);

        field = new Field();
        field.get(5, 3).makeKing();
        assertTrue(field.makeTurn(new Cell(5, 3), new Cell(3, 1)));
        assertTrue(field.makeTurn(new Cell(5, 5), new Cell(4, 6)));
        assertFalse(field.makeTurn(new Cell(3,1), new Cell(3,2)));
        assertFalse(field.makeTurn(new Cell(3,1), new Cell(7,5)));


        //Несколько шашек съедено за один ход
        field = new Field();
        assertTrue(field.makeTurn(new Cell(2, 4), new Cell(3, 5)));
        assertTrue(field.makeTurn(new Cell(5, 5), new Cell(4, 6)));
        assertTrue(field.makeTurn(new Cell(2, 2), new Cell(3, 3)));
        assertTrue(field.makeTurn(new Cell(4, 6), new Cell(2, 4)));
        assertTrue(field.get(3, 5).getChecker() == Checker.empty);
        assertTrue(field.makeTurn(new Cell(2, 4), new Cell(4, 2)));
        assertTrue(field.get(3, 3).getChecker() == Checker.empty);

        field = new Field();
        assertTrue(field.makeTurn(new Cell(2, 2), new Cell(3, 3)));
        assertTrue(field.makeTurn(new Cell(5, 5), new Cell(4, 4)));
        assertTrue(field.makeTurn(new Cell(2, 0), new Cell(3, 1)));
        assertTrue(field.makeTurn(new Cell(6, 6), new Cell(5, 5)));
        assertTrue(field.makeTurn(new Cell(1, 1), new Cell(2, 0)));
        assertTrue(field.makeTurn(new Cell(4, 4), new Cell(2, 2)));
        assertFalse(field.makeTurn(new Cell(2, 2), new Cell(1, 1)));
    }
}