package parser.block;

import parser.expression.LOrExp;

public class Cond {
    private LOrExp lOrExp;

    public Cond(LOrExp lOrExp) {
        this.lOrExp = lOrExp;
    }

    public void print(StringBuilder strb) {
        lOrExp.print(strb);
        strb.append("<Cond>\n");
    }
}
