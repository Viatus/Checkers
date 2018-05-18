package game;

public enum Checker {
    black, white, empty;

    public Checker flip() {
        if (this == Checker.black) {
            return Checker.white;
        }
        return Checker.black;
    }
    public String toString() {
        if (this==black){
            return "черный";
        }
        if (this==white){
            return "белый";
        }
        return "пустой";
    }
}
