package parser.expression;

public class Numbear {
    private String intConst;

    public Numbear(String intConst) {
        this.intConst = intConst;
    }

    public void print(StringBuilder strb) {
        strb.append("INTCON ").append(intConst).append("\n");
        strb.append("<Number>\n");
    }
}
