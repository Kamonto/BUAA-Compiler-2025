package parser.declaration;

import llvmgenerator.LLVMTable;
import parser.expression.ConstExp;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class ConstInitVal {
    private boolean isArray;
    private ConstExp constExp;
    private ArrayList<ConstExp> constExps;

    public ConstInitVal(boolean isArray, ConstExp constExp, ArrayList<ConstExp> constExps) {
        this.isArray = isArray;
        this.constExp = constExp;
        this.constExps = constExps;
    }

    public void print(StringBuilder strb) {
        if (isArray) {
            strb.append("LBRACE {\n");
            int size = constExps.size();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    strb.append("COMMA ,\n");
                }
                constExps.get(i).print(strb);
            }
            strb.append("RBRACE }\n");
        }
        else {
            constExp.print(strb);
        }
        strb.append("<ConstInitVal>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {

    }

    public ArrayList<Integer> calculate(SymbolTable symbols, Scope scope, int size) {
        ArrayList<Integer> values = new ArrayList<Integer>();
        if (isArray) {
            for (ConstExp item : constExps) {
                values.add(item.calculate(symbols, scope));
            }
            for (int i = constExps.size(); i < size; i++) {
                values.add(0);
            }
        }
        else {
            values.add(constExp.calculate(symbols, scope));
        }
        return values;
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {

    }
}
