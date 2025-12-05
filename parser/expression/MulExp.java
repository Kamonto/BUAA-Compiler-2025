package parser.expression;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.*;
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

    public int calculate(SymbolTable symbols, Scope scope) {
        int value = 0;
        int size = unaryExps.size();
        for (int i = 0; i < size; i++) {
            int opType = opTypes.get(i);
            if (opType == 0) {
                value = unaryExps.get(i).calculate(symbols, scope);
            }
            else if (opType == 1) {
                value *= unaryExps.get(i).calculate(symbols, scope);
            }
            else if (opType == 2) {
                value /= unaryExps.get(i).calculate(symbols, scope);
            }
            else if (opType == 3) {
                value %= unaryExps.get(i).calculate(symbols, scope);
            }
        }
        return value;
    }

    public String llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        String reslabel = null;
        int size = unaryExps.size();
        for (int i = 0; i < size; i++) {
            if (opTypes.get(i) == 0) {
                reslabel = unaryExps.get(i).llvmGenerate(symbols, scope, llvms);
            }
            else if (opTypes.get(i) == 1) {
                String label1 = reslabel;
                String label2 = unaryExps.get(i).llvmGenerate(symbols, scope, llvms);
                reslabel = "%" + scope.allocNumber();
                LLVMMul llvmMul = new LLVMMul(reslabel, label1, label2);
                llvms.addLLVM(llvmMul);
            }
            else if (opTypes.get(i) == 2) {
                String label1 = reslabel;
                String label2 = unaryExps.get(i).llvmGenerate(symbols, scope, llvms);
                reslabel = "%" + scope.allocNumber();
                LLVMSdiv llvmSdiv = new LLVMSdiv(reslabel, label1, label2);
                llvms.addLLVM(llvmSdiv);
            }
            else if (opTypes.get(i) == 3) {
                String label1 = reslabel;
                String label2 = unaryExps.get(i).llvmGenerate(symbols, scope, llvms);
                reslabel = "%" + scope.allocNumber();
                LLVMSrem llvmSrem = new LLVMSrem(reslabel, label1, label2);
                llvms.addLLVM(llvmSrem);
            }
        }
        return reslabel;
    }
}
