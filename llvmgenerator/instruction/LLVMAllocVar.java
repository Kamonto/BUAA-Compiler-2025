package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMAllocVar implements LLVM {
    private String label;
    private boolean isPointer;

    public LLVMAllocVar(String label, boolean isPointer) {
        this.label = label;
        this.isPointer = isPointer;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(label);
        strb.append(" = alloca i32");
        if (isPointer) {
            strb.append("*");
        }
        strb.append("\n");
    }
}
