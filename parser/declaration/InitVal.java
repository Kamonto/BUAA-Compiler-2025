package parser.declaration;

import llvmgenerator.LLVMTable;
import parser.expression.ConstExp;
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

    // should promise the value can be calculated!!!
    public ArrayList<Integer> calculate(SymbolTable symbols, Scope scope, int size) {
        ArrayList<Integer> values = new ArrayList<Integer>();
        if (isArray) {
            for (Exp item : exps) {
                values.add(item.calculate(symbols, scope));
            }
            for (int i = exps.size(); i < size; i++) {
                values.add(0);
            }
        }
        else {
            values.add(exp.calculate(symbols, scope));
        }
        return values;
    }

    public ArrayList<String> llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        ArrayList<String> labels = new ArrayList<String>();
        if (isArray) {
            for (Exp item : exps) {
                labels.add(item.llvmGenerate(symbols, scope, llvms));
            }
        }
        else {
            labels.add(exp.llvmGenerate(symbols, scope, llvms));
        }
        return labels;
    }
}
