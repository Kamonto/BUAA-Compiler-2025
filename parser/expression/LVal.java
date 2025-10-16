package parser.expression;

public class LVal {
    private String ident;
    private boolean isArray;
    private Exp exp;

    public LVal(String ident, boolean isArray, Exp exp) {
        this.ident = ident;
        this.isArray = isArray;
        this.exp = exp;
    }

    public void print(StringBuilder strb) {
        strb.append("IDENFR ").append(ident).append("\n");
        if (isArray) {
            strb.append("LBRACK [\n");
            exp.print(strb);
            strb.append("RBRACK ]\n");
        }
        strb.append("<LVal>\n");
    }
}
