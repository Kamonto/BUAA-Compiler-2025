package parser.block;

import lexer.Token;
import parser.expression.Exp;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtReturn implements Stmt {
    private Token returnToken;
    private boolean hasReturnValue;
    private Exp exp;

    public StmtReturn(Token returnToken, boolean hasReturnValue, Exp exp) {
        this.returnToken = returnToken;
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

    public void symbolize(SymbolTable symbols, Scope scope) {
        symbols.checkReturnInVoidFunc(hasReturnValue, returnToken);
        if (hasReturnValue) {
            exp.symbolize(symbols, scope);
        }
    }
}
