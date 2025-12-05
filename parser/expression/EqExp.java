package parser.expression;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMBranch;
import llvmgenerator.instruction.LLVMIcmp;
import llvmgenerator.instruction.LLVMLabel;
import llvmgenerator.instruction.LLVMZext;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class EqExp {
    private ArrayList<RelExp> relExps;
    private ArrayList<Integer> opTypes; // 0: first, 1: ==, 2: !=

    public EqExp(ArrayList<RelExp> relExps, ArrayList<Integer> opTypes) {
        this.relExps = relExps;
        this.opTypes = opTypes;
    }

    public void print(StringBuilder strb) {
        int size = relExps.size();
        for (int i = 0; i < size; i++) {
            int opType = opTypes.get(i);
            if (opType == 1) {
                strb.append("EQL ==\n");
            }
            else if (opType == 2) {
                strb.append("NEQ !=\n");
            }
            relExps.get(i).print(strb);
            strb.append("<EqExp>\n");
        }
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        for (RelExp relExp : relExps) {
            relExp.symbolize(symbols, scope);
        }
    }

    public void llvmGenerate(LLVMLabel trueLabel, LLVMLabel falseLabel, SymbolTable symbols, Scope scope, LLVMTable llvms) {
        String reslabel = null;
        int size = relExps.size();
        for (int i = 0; i < size; i++) {
            if (opTypes.get(i) == 0) {
                reslabel = relExps.get(i).llvmGenerate(symbols, scope, llvms);
            }
            else {
                String label1 = reslabel;
                String label2 = relExps.get(i).llvmGenerate(symbols, scope, llvms);
                String templabel = "%" + scope.allocNumber();
                LLVMIcmp llvmIcmp = new LLVMIcmp(templabel, opTypes.get(i) - 1, label1, label2);
                llvms.addLLVM(llvmIcmp);
                reslabel = "%" + scope.allocNumber();
                LLVMZext llvmZext = new LLVMZext(templabel, reslabel);
                llvms.addLLVM(llvmZext);
            }
        }
        String bitlabel = null;
        if (reslabel != null) {
            bitlabel = "%" + scope.allocNumber();
            LLVMIcmp llvmIcmp = new LLVMIcmp(bitlabel, 1, reslabel, "0");
            llvms.addLLVM(llvmIcmp);
        }
        LLVMBranch llvmBranch = new LLVMBranch(bitlabel, trueLabel, falseLabel);
        llvms.addLLVM(llvmBranch);
    }
}
