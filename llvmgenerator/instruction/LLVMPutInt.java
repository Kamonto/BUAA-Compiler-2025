package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMPutInt implements LLVM {
    private String label;

    public LLVMPutInt(String label) {
        this.label = label;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("call void @putint(i32 ");
        strb.append(label);
        strb.append(")\n");
    }
}
