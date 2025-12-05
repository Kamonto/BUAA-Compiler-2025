package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMBranch implements LLVM {
    private String bitlabel;
    private LLVMLabel label1;
    private LLVMLabel label2;

    public LLVMBranch(String bitlabel, LLVMLabel label1, LLVMLabel label2) {
        this.bitlabel = bitlabel;
        this.label1 = label1;
        this.label2 = label2;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("br i1 ");
        strb.append(bitlabel);
        strb.append(", label %");
        strb.append(label1.getNumber());
        strb.append(", label %");
        strb.append(label2.getNumber());
        strb.append("\n");
    }
}
