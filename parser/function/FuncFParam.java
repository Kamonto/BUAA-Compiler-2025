package parser.function;

import parser.type.BType;

public class FuncFParam {
    private BType bType;
    private String ident;
    private boolean isArray;

    public FuncFParam(BType bType, String ident, boolean isArray) {
        this.bType = bType;
        this.ident = ident;
        this.isArray = isArray;
    }

    public void print(StringBuilder strb) {
        bType.print(strb);
        strb.append("IDENFR ").append(ident).append("\n");
        if (isArray) {
            strb.append("LBRACK [\n");
            strb.append("RBRACK ]\n");
        }
        strb.append("<FuncFParam>\n");
    }
}
