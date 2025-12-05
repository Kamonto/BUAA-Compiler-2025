package parser.expression;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMLabel;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class LAndExp {
    private ArrayList<EqExp> eqExps;

    public LAndExp(ArrayList<EqExp> eqExps) {
        this.eqExps = eqExps;
    }

    public void print(StringBuilder strb) {
        int size = eqExps.size();
        if (size > 0) {
            eqExps.get(0).print(strb);
            strb.append("<LAndExp>\n");
            for (int i = 1; i < size; i++) {
                strb.append("AND &&\n");
                eqExps.get(i).print(strb);
                strb.append("<LAndExp>\n");
            }
        }
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        for (EqExp eqExp : eqExps) {
            eqExp.symbolize(symbols, scope);
        }
    }

    public void llvmGenerate(LLVMLabel trueLabel, LLVMLabel falseLabel, SymbolTable symbols, Scope scope, LLVMTable llvms) {
        ArrayList<LLVMLabel> trueLabels = new ArrayList<LLVMLabel>();
        int size = eqExps.size();
        for (int i = 0; i < size - 1; i++) {
            trueLabels.add(new LLVMLabel());
        }
        trueLabels.add(trueLabel);
        for (int i = 0; i < size; i++) {
            eqExps.get(i).llvmGenerate(trueLabels.get(i), falseLabel, symbols, scope, llvms);
            if (i < size - 1) {
                trueLabels.get(i).setNumber(scope.allocNumber());
                llvms.addLLVM(trueLabels.get(i));
            }
        }
    }
}
