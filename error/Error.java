package error;

public class Error {
    private int line;
    private char type;

    public Error(int line, char type) {
        this.line = line;
        this.type = type;
    }

    public int getLine() {
        return line;
    }

    public char getType() {
        return type;
    }
}
