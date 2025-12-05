package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMJump implements LLVM {
    private LLVMLabel label;

    public LLVMJump(LLVMLabel label) {
        this.label = label;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("br label %");
        strb.append(label.getNumber());
        strb.append("\n");
    }
}
