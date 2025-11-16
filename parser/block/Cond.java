package parser.block;

import parser.expression.LOrExp;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class Cond {
    private LOrExp lOrExp;

    public Cond(LOrExp lOrExp) {
        this.lOrExp = lOrExp;
    }

    public void print(StringBuilder strb) {
        lOrExp.print(strb);
        strb.append("<Cond>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        lOrExp.symbolize(symbols, scope);
    }
}
