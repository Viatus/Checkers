package game;

import org.junit.Test;

import static org.junit.Assert.*;

public class FieldTest {
    @Test
    public void makeTurn() {
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

    }
}