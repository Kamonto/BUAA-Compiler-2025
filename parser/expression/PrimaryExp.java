package parser.expression;

import symbolizer.FuncSymbol;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class PrimaryExp {
    private int type; // 0: '('Exp')', 1: LVal, 2: Number
    private Exp exp;
    private LVal lVal;
    private Numbear numbear;

    public PrimaryExp(int type, Exp exp, LVal lVal, Numbear numbear) {
        this.type = type;
        this.exp = exp;
        this.lVal = lVal;
        this.numbear = numbear;
    }

    public void print(StringBuilder strb) {
        if (type == 0) {
            strb.append("LPARENT (\n");
            exp.print(strb);
            strb.append("RPARENT )\n");
        }
        else if (type == 1) {
            lVal.print(strb);
        }
        else if (type == 2) {
            numbear.print(strb);
        }
        strb.append("<PrimaryExp>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        if (type == 0) {
            exp.symbolize(symbols, scope);
        }
        else if (type == 1) {
            lVal.symbolize(false, symbols, scope);
        }
    }

    public boolean isArray(SymbolTable symbols, Scope scope) {
        if (type == 0) {
            return exp.isArray(symbols, scope);
        }
        else if (type == 1) {
            return lVal.isArray(symbols, scope);
        }
        else if (type == 2) {
            return false;
        }
        return false;
    }
}
