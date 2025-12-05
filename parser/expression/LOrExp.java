package parser.expression;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMLabel;
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

    public void llvmGenerate(LLVMLabel trueLabel, LLVMLabel falseLabel, SymbolTable symbols, Scope scope, LLVMTable llvms) {
        ArrayList<LLVMLabel> falseLabels = new ArrayList<LLVMLabel>();
        int size = lAndExps.size();
        for (int i = 0; i < size - 1; i++) {
            falseLabels.add(new LLVMLabel());
        }
        falseLabels.add(falseLabel);
        for (int i = 0; i < size; i++) {
            lAndExps.get(i).llvmGenerate(trueLabel, falseLabels.get(i), symbols, scope, llvms);
            if (i < size - 1) {
                falseLabels.get(i).setNumber(scope.allocNumber());
                llvms.addLLVM(falseLabels.get(i));
            }
        }
    }
}
