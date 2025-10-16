package parser.declaration;

import parser.expression.ConstExp;

public class VarDef {
    private String ident;
    private boolean isArray;
    private ConstExp constExp;
    private boolean hasInitValue;
    private InitVal initVal;

    public VarDef(String ident, boolean isArray, ConstExp constExp, boolean hasInitValue, InitVal initVal) {
        this.ident = ident;
        this.isArray = isArray;
        this.constExp = constExp;
        this.hasInitValue = hasInitValue;
        this.initVal = initVal;
    }

    public void print(StringBuilder strb) {
        strb.append("IDENFR ").append(ident).append("\n");
        if (isArray) {
            strb.append("LBRACK [\n");
            constExp.print(strb);
            strb.append("RBRACK ]\n");
        }
        if (hasInitValue) {
            strb.append("ASSIGN =\n");
            initVal.print(strb);
        }
        strb.append("<VarDef>\n");
    }
}
