package parser.expression;

import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class Numbear {
    private String intConst;

    public Numbear(String intConst) {
        this.intConst = intConst;
    }

    public void print(StringBuilder strb) {
        strb.append("INTCON ").append(intConst).append("\n");
        strb.append("<Number>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {

    }
}
