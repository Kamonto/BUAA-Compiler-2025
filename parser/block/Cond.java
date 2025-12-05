package parser.block;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMLabel;
import parser.expression.LOrExp;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class Cond {
    private LOrExp lOrExp;

    public Cond(LOrExp lOrExp) {
        this.lOrExp = lOrExp;
    }

    public void print(StringBuilder strb) {
        lOrExp.print(strb);
        strb.append("<Cond>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        lOrExp.symbolize(symbols, scope);
    }

    public void llvmGenerate(LLVMLabel trueLabel, LLVMLabel falseLabel, SymbolTable symbols, Scope scope, LLVMTable llvms) {
        lOrExp.llvmGenerate(trueLabel, falseLabel, symbols, scope, llvms);
    }
}
