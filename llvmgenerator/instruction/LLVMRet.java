package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMRet implements LLVM {
    boolean hasReturnValue;
    String label;

    public LLVMRet(boolean hasReturnValue, String label) {
        this.hasReturnValue = hasReturnValue;
        this.label = label;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("ret ");
        if (hasReturnValue) {
            strb.append("i32 ");
            strb.append(label);
        }
        else {
            strb.append("void");
        }
        strb.append("\n");
    }
}
