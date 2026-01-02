package parser.block;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMMove;
import llvmgenerator.instruction.LLVMStore;
import parser.expression.Exp;
import parser.expression.LVal;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;


public class ForStmt {
    private ArrayList<LVal> lVals;
    private ArrayList<Exp> exps;

    public ForStmt(ArrayList<LVal> lVals, ArrayList<Exp> exps) {
        this.lVals = lVals;
        this.exps = exps;
    }

    public void print(StringBuilder strb) {
        int size = lVals.size();
        if (size > 0) {
            lVals.get(0).print(strb);
            strb.append("ASSIGN =\n");
            exps.get(0).print(strb);
            for (int i = 1; i < size; i++) {
                strb.append("COMMA ,\n");
                lVals.get(i).print(strb);
                strb.append("ASSIGN =\n");
                exps.get(i).print(strb);
            }
        }
        strb.append("<ForStmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        for (LVal lVal : lVals) {
            lVal.symbolize(true, symbols, scope);
        }
        for (Exp exp : exps) {
            exp.symbolize(symbols, scope);
        }
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        int size = lVals.size();
        for (int i = 0; i < size; i++) {
            String dstlabel = lVals.get(i).llvmGenerate(false, symbols, scope, llvms);
            String srclabel = exps.get(i).llvmGenerate(symbols, scope, llvms);
            if (lVals.get(i).getIsArray()) {
                LLVMStore llvmStore = new LLVMStore(srclabel, dstlabel, false);
                llvms.addLLVM(llvmStore);
            }
            else {
                LLVMMove llvmMove = new LLVMMove(srclabel, dstlabel);
                llvms.addLLVM(llvmMove);
            }
        }
    }
}
