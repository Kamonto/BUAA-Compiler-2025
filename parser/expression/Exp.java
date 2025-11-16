package parser.expression;

import symbolizer.FuncSymbol;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class Exp {
    private AddExp addExp;

    public Exp (AddExp addExp) {
        this.addExp = addExp;
    }

    public void print(StringBuilder strb) {
        addExp.print(strb);
        strb.append("<Exp>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        addExp.symbolize(symbols, scope);
    }

    public boolean isArray(SymbolTable symbols, Scope scope) {
        return addExp.isArray(symbols, scope);
    }
}
