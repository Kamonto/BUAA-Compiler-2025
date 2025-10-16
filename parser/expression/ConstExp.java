package parser.expression;

public class ConstExp {
    private AddExp addExp;

    public ConstExp (AddExp addExp) {
        this.addExp = addExp;
    }

    public void print(StringBuilder strb) {
        addExp.print(strb);
        strb.append("<ConstExp>\n");
    }
}
