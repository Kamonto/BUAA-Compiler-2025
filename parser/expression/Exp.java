package parser.expression;

public class Exp {
    private AddExp addExp;

    public Exp (AddExp addExp) {
        this.addExp = addExp;
    }

    public void print(StringBuilder strb) {
        addExp.print(strb);
        strb.append("<Exp>\n");
    }
}
