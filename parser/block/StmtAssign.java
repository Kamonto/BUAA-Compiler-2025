package parser.block;

import parser.expression.Exp;
import parser.expression.LVal;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtAssign implements Stmt {
    private LVal lVal;
    private Exp exp;

    public StmtAssign(LVal lVal, Exp exp) {
        this.lVal = lVal;
        this.exp = exp;
    }

    public void print(StringBuilder strb) {
        lVal.print(strb);
        strb.append("ASSIGN =\n");
        exp.print(strb);
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        lVal.symbolize(true, symbols, scope);
        exp.symbolize(symbols, scope);
    }
}
