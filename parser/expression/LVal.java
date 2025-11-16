package parser.expression;

import lexer.Token;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class LVal {
    private Token ident;
    private boolean isArray;
    private Exp exp;

    public LVal(Token ident, boolean isArray, Exp exp) {
        this.ident = ident;
        this.isArray = isArray;
        this.exp = exp;
    }

    public void print(StringBuilder strb) {
        strb.append("IDENFR ").append(ident.getContent()).append("\n");
        if (isArray) {
            strb.append("LBRACK [\n");
            exp.print(strb);
            strb.append("RBRACK ]\n");
        }
        strb.append("<LVal>\n");
    }

    public void symbolize(boolean isAssign, SymbolTable symbols, Scope scope) {
        symbols.checkUndeclaredVariable(ident);
        if (isAssign) {
            symbols.checkAssignToConst(ident);
        }
        if (isArray) {
            exp.symbolize(symbols, scope);
        }
    }

    public boolean isArray(SymbolTable symbols, Scope scope) {
        if (isArray) {
            return false;
        }
        else {
            return symbols.isArray(scope, ident);
        }
    }
}
