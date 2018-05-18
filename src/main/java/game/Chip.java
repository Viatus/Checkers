package game;

public class Chip {
    private boolean isKing;

    private Checker checker;

    public boolean getIsKing() {
        return isKing;
    }

    public Checker getChecker() {
        return checker;
    }

    public Chip() {
        checker = Checker.empty;
        isKing = false;
    }

    public Chip(Checker checker) {
        this.checker = checker;
        isKing = false;
    }

    public void makeKing() {
        if (checker == Checker.white || checker == Checker.black) {
            isKing = true;
        }
    }

    public boolean isOpposite(Chip other) {
        return !((this.checker == Checker.white && other.checker == Checker.white)
                || (this.checker == Checker.black && other.checker == Checker.black)
                || (this.checker == Checker.empty && other.checker == Checker.empty));
    }

}
