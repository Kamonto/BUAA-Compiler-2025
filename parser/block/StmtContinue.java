package parser.block;

import lexer.Token;
import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMJump;
import llvmgenerator.instruction.LLVMLabel;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtContinue implements Stmt {
    private Token continueToken;

    public StmtContinue(Token continueToken) {
        this.continueToken = continueToken;
    }

    public void print(StringBuilder strb) {
        strb.append("CONTINUETK continue\n");
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        symbols.checkContinueOrBreakOutOfLoop(continueToken);
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        LLVMLabel loopEndLabel = scope.getLoopEndLabel();
        LLVMJump llvmJump = new LLVMJump(loopEndLabel, llvms.getMergedllvms());
        llvms.addLLVM(llvmJump);
        LLVMLabel llvmLabel = new LLVMLabel();
        llvmLabel.setNumber(scope.allocNumber());
        llvms.addLLVM(llvmLabel);
    }
}
