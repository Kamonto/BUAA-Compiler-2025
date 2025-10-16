package error;

public class Error implements Comparable<Error> {
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

    @Override
    public int compareTo(Error error) {
        return Integer.compare(this.line, error.line);
    }
}
