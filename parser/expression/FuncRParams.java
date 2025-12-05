package parser.expression;

import llvmgenerator.LLVMTable;
import parser.function.FuncFParam;
import symbolizer.FuncSymbol;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class FuncRParams {
    private ArrayList<Exp> exps;

    public FuncRParams(ArrayList<Exp> exps) {
        this.exps = exps;
    }

    public void print(StringBuilder strb) {
        int size = exps.size();
        if (size > 0) {
            exps.get(0).print(strb);
            for (int i = 1; i < size; i++) {
                strb.append("COMMA ,\n");
                exps.get(i).print(strb);
            }
        }
        strb.append("<FuncRParams>\n");
    }

    public void symbolize(FuncSymbol funcSymbol, SymbolTable symbols, Scope scope) {
        for (Exp exp : exps) {
            funcSymbol.addParam(exp.isArray(symbols, scope));
        }
    }

    public void llvmGenerate(ArrayList<String> paramLabels, SymbolTable symbols, Scope scope, LLVMTable llvms) {
        for (Exp exp : exps) {
            paramLabels.add(exp.llvmGenerate(symbols, scope, llvms));
        }
    }
}
