package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMAllocArr implements LLVM {
    private String label;
    private int size;

    public LLVMAllocArr(String label, int size) {
        this.label = label;
        this.size = size;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(label);
        strb.append(" = alloca [");
        strb.append(size);
        strb.append(" x i32]\n");
    }
}
