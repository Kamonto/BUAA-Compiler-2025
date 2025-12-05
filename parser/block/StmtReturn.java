package parser.block;

import lexer.Token;
import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMLabel;
import llvmgenerator.instruction.LLVMRet;
import parser.expression.Exp;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtReturn implements Stmt {
    private Token returnToken;
    private boolean hasReturnValue;
    private Exp exp;

    public StmtReturn(Token returnToken, boolean hasReturnValue, Exp exp) {
        this.returnToken = returnToken;
        this.hasReturnValue = hasReturnValue;
        this.exp = exp;
    }

    public void print(StringBuilder strb) {
        strb.append("RETURNTK return\n");
        if (hasReturnValue) {
            exp.print(strb);
        }
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        symbols.checkReturnInVoidFunc(hasReturnValue, returnToken);
        if (hasReturnValue) {
            exp.symbolize(symbols, scope);
        }
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        if (hasReturnValue) {
            LLVMRet llvmRet = new LLVMRet(true, exp.llvmGenerate(symbols, scope, llvms));
            llvms.addLLVM(llvmRet);
        }
        else {
            LLVMRet llvmRet = new LLVMRet(false, null);
            llvms.addLLVM(llvmRet);
        }
        LLVMLabel llvmLabel = new LLVMLabel();
        llvmLabel.setNumber(scope.allocNumber());
        llvms.addLLVM(llvmLabel);
    }
}
