package game;

public class Cell {
    private int x;

    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof Cell) {
            Cell cell = (Cell) other;
            return x == cell.x && y == cell.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 11;
        result = 19 * result + x;
        return 19 * result + y;
    }
}
