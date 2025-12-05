package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMSrem implements LLVM {
    private String reslabel;
    private String label1;
    private String label2;

    public LLVMSrem(String reslabel, String label1, String label2) {
        this.reslabel = reslabel;
        this.label1 = label1;
        this.label2 = label2;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(reslabel);
        strb.append(" = srem i32 ");
        strb.append(label1);
        strb.append(", ");
        strb.append(label2);
        strb.append("\n");
    }
}
