package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMGetInt implements LLVM {
    private String label;

    public LLVMGetInt(String label) {
        this.label = label;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(label);
        strb.append(" = call i32 @getint()\n");
    }
}
