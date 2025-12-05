package parser.expression;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMAdd;
import llvmgenerator.instruction.LLVMSub;
import symbolizer.FuncSymbol;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class AddExp {
    private ArrayList<MulExp> mulExps;
    private ArrayList<Integer> opTypes; // 0: first, 1: +, 2: -

    public AddExp(ArrayList<MulExp> mulExps, ArrayList<Integer> opTypes) {
        this.mulExps = mulExps;
        this.opTypes = opTypes;
    }

    public void print(StringBuilder strb) {
        int size = mulExps.size();
        for (int i = 0; i < size; i++) {
            int opType = opTypes.get(i);
            if (opType == 1) {
                strb.append("PLUS +\n");
            }
            else if (opType == 2) {
                strb.append("MINU -\n");
            }
            mulExps.get(i).print(strb);
            strb.append("<AddExp>\n");
        }
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        for (MulExp mulExp : mulExps) {
            mulExp.symbolize(symbols, scope);
        }
    }

    public boolean isArray(SymbolTable symbols, Scope scope) {
        for (MulExp mulExp : mulExps) {
            if (mulExp.isArray(symbols, scope)) {
                return true;
            }
        }
        return false;
    }

    public int calculate(SymbolTable symbols, Scope scope) {
        int value = 0;
        int size = mulExps.size();
        for (int i = 0; i < size; i++) {
            int opType = opTypes.get(i);
            if (opType == 0) {
                value = mulExps.get(i).calculate(symbols, scope);
            }
            else if (opType == 1) {
                value += mulExps.get(i).calculate(symbols, scope);
            }
            else if (opType == 2) {
                value -= mulExps.get(i).calculate(symbols, scope);
            }
        }
        return value;
    }

    public String llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        String reslabel = null;
        int size = mulExps.size();
        for (int i = 0; i < size; i++) {
            if (opTypes.get(i) == 0) {
                reslabel = mulExps.get(i).llvmGenerate(symbols, scope, llvms);
            }
            else if (opTypes.get(i) == 1) {
                String label1 = reslabel;
                String label2 = mulExps.get(i).llvmGenerate(symbols, scope, llvms);
                reslabel = "%" + scope.allocNumber();
                LLVMAdd llvmAdd = new LLVMAdd(reslabel, label1, label2);
                llvms.addLLVM(llvmAdd);
            }
            else if (opTypes.get(i) == 2) {
                String label1 = reslabel;
                String label2 = mulExps.get(i).llvmGenerate(symbols, scope, llvms);
                reslabel = "%" + scope.allocNumber();
                LLVMSub llvmSub = new LLVMSub(reslabel, label1, label2);
                llvms.addLLVM(llvmSub);
            }
        }
        return reslabel;
    }
}
