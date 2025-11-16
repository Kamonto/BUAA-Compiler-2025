package parser.expression;

import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class UnaryOp {
    private int type; // 1: +, -1: -, 0: !

    public UnaryOp(int type) {
        this.type = type;
    }

    public void print(StringBuilder strb) {
        if (type == 1) {
            strb.append("PLUS +\n");
        }
        else if (type == -1) {
            strb.append("MINU -\n");
        }
        else if (type == 0) {
            strb.append("NOT !\n");
        }
        strb.append("<UnaryOp>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {

    }
}
