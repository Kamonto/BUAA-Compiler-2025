package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMDefGlobalVar implements LLVM {
    private String label;
    private int value;

    public LLVMDefGlobalVar(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public void print(StringBuilder strb) {
        strb.append(label);
        strb.append(" = dso_local global i32 ");
        strb.append(value);
        strb.append("\n");
    }
}
