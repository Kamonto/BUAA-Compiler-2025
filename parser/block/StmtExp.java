package parser.block;

import llvmgenerator.LLVMTable;
import parser.expression.Exp;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtExp implements Stmt {
    private boolean hasExp;
    private Exp exp;

    public StmtExp(boolean hasExp, Exp exp) {
        this.hasExp = hasExp;
        this.exp = exp;
    }

    public void print(StringBuilder strb) {
        if (hasExp) {
            exp.print(strb);
        }
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        if (hasExp) {
            exp.symbolize(symbols, scope);
        }
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        if (hasExp) {
            exp.llvmGenerate(symbols, scope ,llvms);
        }
    }
}
