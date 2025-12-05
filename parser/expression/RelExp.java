package parser.expression;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMAdd;
import llvmgenerator.instruction.LLVMIcmp;
import llvmgenerator.instruction.LLVMSub;
import llvmgenerator.instruction.LLVMZext;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class RelExp {
    private ArrayList<AddExp> addExps;
    private ArrayList<Integer> opTypes; // 0: first, 1: <, 2: >, 3: <=, 4: >=

    public RelExp(ArrayList<AddExp> addExps, ArrayList<Integer> opTypes) {
        this.addExps = addExps;
        this.opTypes = opTypes;
    }

    public void print(StringBuilder strb) {
        int size = addExps.size();
        for (int i = 0; i < size; i++) {
            int opType = opTypes.get(i);
            if (opType == 1) {
                strb.append("LSS <\n");
            }
            else if (opType == 2) {
                strb.append("GRE >\n");
            }
            else if (opType == 3) {
                strb.append("LEQ <=\n");
            }
            else if (opType == 4) {
                strb.append("GEQ >=\n");
            }
            addExps.get(i).print(strb);
            strb.append("<RelExp>\n");
        }
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        for (AddExp addExp : addExps) {
            addExp.symbolize(symbols, scope);
        }
    }

    public String llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        String reslabel = null;
        int size = addExps.size();
        for (int i = 0; i < size; i++) {
            if (opTypes.get(i) == 0) {
                reslabel = addExps.get(i).llvmGenerate(symbols, scope, llvms);
            }
            else {
                String label1 = reslabel;
                String label2 = addExps.get(i).llvmGenerate(symbols, scope, llvms);
                String templabel = "%" + scope.allocNumber();
                LLVMIcmp llvmIcmp = new LLVMIcmp(templabel, opTypes.get(i) + 1, label1, label2);
                llvms.addLLVM(llvmIcmp);
                reslabel = "%" + scope.allocNumber();
                LLVMZext llvmZext = new LLVMZext(templabel, reslabel);
                llvms.addLLVM(llvmZext);
            }
        }
        return reslabel;
    }
}
