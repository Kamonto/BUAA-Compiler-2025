package parser.block;

import lexer.Token;
import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMJump;
import llvmgenerator.instruction.LLVMLabel;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtBreak implements Stmt {
    private Token breakToken;

    public StmtBreak(Token breakToken) {
        this.breakToken = breakToken;
    }

    public void print(StringBuilder strb) {
        strb.append("BREAKTK break\n");
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        symbols.checkContinueOrBreakOutOfLoop(breakToken);
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        LLVMLabel endLabel = scope.getEndLabel();
        LLVMJump llvmJump = new LLVMJump(endLabel, llvms.getMergedllvms());
        llvms.addLLVM(llvmJump);
        LLVMLabel llvmLabel = new LLVMLabel();
        llvmLabel.setNumber(scope.allocNumber());
        llvms.addLLVM(llvmLabel);
    }
}
