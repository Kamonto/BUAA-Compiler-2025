package parser.block;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMStore;
import parser.expression.Exp;
import parser.expression.LVal;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtAssign implements Stmt {
    private LVal lVal;
    private Exp exp;

    public StmtAssign(LVal lVal, Exp exp) {
        this.lVal = lVal;
        this.exp = exp;
    }

    public void print(StringBuilder strb) {
        lVal.print(strb);
        strb.append("ASSIGN =\n");
        exp.print(strb);
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        lVal.symbolize(true, symbols, scope);
        exp.symbolize(symbols, scope);
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        String dstlabel = lVal.llvmGenerate(false, symbols, scope, llvms);
        String srclabel = exp.llvmGenerate(symbols, scope, llvms);
        LLVMStore llvmStore = new LLVMStore(srclabel, dstlabel, false);
        llvms.addLLVM(llvmStore);
    }
}
