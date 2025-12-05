package parser.block;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMJump;
import llvmgenerator.instruction.LLVMLabel;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtIf implements Stmt {
    private Cond cond;
    private Stmt stmt;
    private boolean hasElse;
    private Stmt anostmt;

    public StmtIf(Cond cond, Stmt stmt, boolean hasElse, Stmt anostmt) {
        this.cond = cond;
        this.stmt = stmt;
        this.hasElse = hasElse;
        this.anostmt = anostmt;
    }

    public void print(StringBuilder strb) {
        strb.append("IFTK if\n");
        strb.append("LPARENT (\n");
        cond.print(strb);
        strb.append("RPARENT )\n");
        stmt.print(strb);
        if (hasElse) {
            strb.append("ELSETK else\n");
            anostmt.print(strb);
        }
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        stmt.symbolize(symbols, scope);
        if (hasElse) {
            anostmt.symbolize(symbols, scope);
        }
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        LLVMLabel trueLabel = new LLVMLabel();
        LLVMLabel endLabel = new LLVMLabel();
        if (hasElse) {
            LLVMLabel falseLabel = new LLVMLabel();
            cond.llvmGenerate(trueLabel, falseLabel, symbols, scope, llvms);
            trueLabel.setNumber(scope.allocNumber());
            llvms.addLLVM(trueLabel);
            stmt.llvmGenerate(symbols, scope, llvms);
            LLVMJump llvmJump = new LLVMJump(endLabel);
            llvms.addLLVM(llvmJump);
            falseLabel.setNumber(scope.allocNumber());
            llvms.addLLVM(falseLabel);
            anostmt.llvmGenerate(symbols, scope, llvms);
            LLVMJump anollvmJump = new LLVMJump(endLabel);
            llvms.addLLVM(anollvmJump);
        }
        else {
            cond.llvmGenerate(trueLabel, endLabel, symbols, scope, llvms);
            trueLabel.setNumber(scope.allocNumber());
            llvms.addLLVM(trueLabel);
            stmt.llvmGenerate(symbols, scope, llvms);
            LLVMJump llvmJump = new LLVMJump(endLabel);
            llvms.addLLVM(llvmJump);
        }
        endLabel.setNumber(scope.allocNumber());
        llvms.addLLVM(endLabel);
    }
}
