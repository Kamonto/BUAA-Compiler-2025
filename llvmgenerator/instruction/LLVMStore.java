package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMStore implements LLVM {
    private String srclabel;
    private String dstlabel;
    private boolean isPointer;

    public LLVMStore(String srclabel, String dstlabel, boolean isPointer) {
        this.srclabel = srclabel;
        this.dstlabel = dstlabel;
        this.isPointer = isPointer;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("store i32");
        if (isPointer) {
            strb.append("*");
        }
        strb.append(" ");
        strb.append(srclabel);
        strb.append(", i32*");
        if (isPointer) {
            strb.append("*");
        }
        strb.append(" ");
        strb.append(dstlabel);
        strb.append("\n");
    }
}
