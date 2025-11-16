package parser.expression;

import symbolizer.FuncSymbol;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class MulExp {
    private ArrayList<UnaryExp> unaryExps;
    private ArrayList<Integer> opTypes; // 0: first, 1: *, 2: /, 3: %

    public MulExp(ArrayList<UnaryExp> unaryExps, ArrayList<Integer> opTypes) {
        this.unaryExps = unaryExps;
        this.opTypes = opTypes;
    }

    public void print(StringBuilder strb) {
        int size = unaryExps.size();
        for (int i = 0; i < size; i++) {
            int opType = opTypes.get(i);
            if (opType == 1) {
                strb.append("MULT *\n");
            }
            else if (opType == 2) {
                strb.append("DIV /\n");
            }
            else if (opType == 3) {
                strb.append("MOD %\n");
            }
            unaryExps.get(i).print(strb);
            strb.append("<MulExp>\n");
        }
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        for (UnaryExp unaryExp : unaryExps) {
            unaryExp.symbolize(symbols, scope);
        }
    }

    public boolean isArray(SymbolTable symbols, Scope scope) {
        for (UnaryExp unaryExp : unaryExps) {
            if (unaryExp.isArray(symbols, scope)) {
                return true;
            }
        }
        return false;
    }
}
