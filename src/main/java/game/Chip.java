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

}
