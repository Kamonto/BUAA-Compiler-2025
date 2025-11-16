package parser.expression;

import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class LOrExp {
    private ArrayList<LAndExp> lAndExps;

    public LOrExp(ArrayList<LAndExp> lAndExps) {
        this.lAndExps = lAndExps;
    }

    public void print(StringBuilder strb) {
        int size = lAndExps.size();
        if (size > 0) {
            lAndExps.get(0).print(strb);
            strb.append("<LOrExp>\n");
            for (int i = 1; i < size; i++) {
                strb.append("OR ||\n");
                lAndExps.get(i).print(strb);
                strb.append("<LOrExp>\n");
            }
        }
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        for (LAndExp lAndExp : lAndExps) {
            lAndExp.symbolize(symbols, scope);
        }
    }
}
