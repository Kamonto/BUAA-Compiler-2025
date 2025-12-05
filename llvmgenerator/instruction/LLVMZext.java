package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMZext implements LLVM {
    private String srclabel;
    private String dstlabel;

    public LLVMZext(String srclabel, String dstlabel) {
        this.srclabel = srclabel;
        this.dstlabel = dstlabel;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(dstlabel);
        strb.append(" = zext i1 ");
        strb.append(srclabel);
        strb.append(" to i32\n");
    }
}
