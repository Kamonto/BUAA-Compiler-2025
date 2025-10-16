package parser.block;

import parser.expression.Exp;

public class StmtExp implements Stmt {
    private boolean hasExp;
    private Exp exp;

    public StmtExp(boolean hasExp, Exp exp) {
        this.hasExp = hasExp;
        this.exp = exp;
    }

    public void print(StringBuilder strb) {
        if (hasExp) {
            exp.print(strb);
        }
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }
}
