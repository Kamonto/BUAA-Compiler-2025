package parser.declaration;

import parser.expression.ConstExp;

public class ConstDef {
    private String ident;
    private boolean isArray;
    private ConstExp constExp;
    private ConstInitVal constInitVal;

    public ConstDef(String ident, boolean isArray, ConstExp constExp, ConstInitVal constInitVal) {
        this.ident = ident;
        this.isArray = isArray;
        this.constExp = constExp;
        this.constInitVal = constInitVal;
    }

    public void print(StringBuilder strb) {
        strb.append("IDENFR ").append(ident).append("\n");
        if (isArray) {
            strb.append("LBRACK [\n");
            constExp.print(strb);
            strb.append("RBRACK ]\n");
        }
        strb.append("ASSIGN =\n");
        constInitVal.print(strb);
        strb.append("<ConstDef>\n");
    }
}
