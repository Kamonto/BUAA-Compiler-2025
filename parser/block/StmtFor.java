package parser.block;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMJump;
import llvmgenerator.instruction.LLVMLabel;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtFor implements Stmt {
    private boolean hasFormerForStmt;
    private ForStmt forStmt;
    private boolean hasCond;
    private Cond cond;
    private boolean hasLatterForStmt;
    private ForStmt anoForStmt;
    private Stmt stmt;

    public StmtFor(boolean hasFormerForStmt, ForStmt forStmt, boolean hasCond, Cond cond,
                   boolean hasLatterForStmt, ForStmt anoForStmt, Stmt stmt) {
        this.hasFormerForStmt = hasFormerForStmt;
        this.forStmt = forStmt;
        this.hasCond = hasCond;
        this.cond = cond;
        this.hasLatterForStmt = hasLatterForStmt;
        this.anoForStmt = anoForStmt;
        this.stmt = stmt;
    }

    public void print(StringBuilder strb) {
        strb.append("FORTK for\n");
        strb.append("LPARENT (\n");
        if (hasFormerForStmt) {
            forStmt.print(strb);
        }
        strb.append("SEMICN ;\n");
        if (hasCond) {
            cond.print(strb);
        }
        strb.append("SEMICN ;\n");
        if (hasLatterForStmt) {
            anoForStmt.print(strb);
        }
        strb.append("RPARENT )\n");
        stmt.print(strb);
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        if (hasFormerForStmt) {
            forStmt.symbolize(symbols, scope);
        }
        if (hasCond) {
            cond.symbolize(symbols, scope);
        }
        if (hasLatterForStmt) {
            anoForStmt.symbolize(symbols, scope);
        }
        scope.entryLoop();
        stmt.symbolize(symbols, scope);
        scope.exitLoop();
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        if (hasFormerForStmt) {
            forStmt.llvmGenerate(symbols, scope, llvms);
        }
        LLVMLabel beginLabel = new LLVMLabel();
        LLVMLabel loopEndLabel = new LLVMLabel();
        LLVMLabel endLabel = new LLVMLabel();
        scope.loopEndLabelStackPush(loopEndLabel);
        scope.endLabelStackPush(endLabel);
        LLVMJump llvmJump1 = new LLVMJump(beginLabel, llvms.getMergedllvms());
        llvms.addLLVM(llvmJump1);
        beginLabel.setNumber(scope.allocNumber());
        llvms.addLLVM(beginLabel);
        if (hasCond) {
            LLVMLabel loopLabel = new LLVMLabel();
            cond.llvmGenerate(loopLabel, endLabel, symbols, scope, llvms);
            loopLabel.setNumber(scope.allocNumber());
            llvms.addLLVM(loopLabel);
        }
        stmt.llvmGenerate(symbols, scope, llvms);
        LLVMJump llvmJump2 = new LLVMJump(loopEndLabel, llvms.getMergedllvms());
        llvms.addLLVM(llvmJump2);
        loopEndLabel.setNumber(scope.allocNumber());
        llvms.addLLVM(loopEndLabel);
        if (hasLatterForStmt) {
            anoForStmt.llvmGenerate(symbols, scope, llvms);
        }
        LLVMJump llvmJump3 = new LLVMJump(beginLabel, llvms.getMergedllvms());
        llvms.addLLVM(llvmJump3);
        endLabel.setNumber(scope.allocNumber());
        llvms.addLLVM(endLabel);
        scope.loopEndLabelStackPop();
        scope.endLabelStackPop();
    }
}
