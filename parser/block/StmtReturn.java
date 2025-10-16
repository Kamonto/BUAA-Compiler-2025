package parser.block;

import parser.expression.Exp;

public class StmtReturn implements Stmt {
    private boolean hasReturnValue;
    private Exp exp;

    public StmtReturn(boolean hasReturnValue, Exp exp) {
        this.hasReturnValue = hasReturnValue;
        this.exp = exp;
    }

    public void print(StringBuilder strb) {
        strb.append("RETURNTK return\n");
        if (hasReturnValue) {
            exp.print(strb);
        }
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }
}
