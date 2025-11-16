package parser.declaration;

import parser.expression.Exp;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class InitVal {
    private boolean isArray;
    private Exp exp;
    private ArrayList<Exp> exps;

    public InitVal(boolean isArray, Exp exp, ArrayList<Exp> exps) {
        this.isArray = isArray;
        this.exp = exp;
        this.exps = exps;
    }

    public void print(StringBuilder strb) {
        if (isArray) {
            strb.append("LBRACE {\n");
            int size = exps.size();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    strb.append("COMMA ,\n");
                }
                exps.get(i).print(strb);
            }
            strb.append("RBRACE }\n");
        }
        else {
            exp.print(strb);
        }
        strb.append("<InitVal>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {

    }
}
