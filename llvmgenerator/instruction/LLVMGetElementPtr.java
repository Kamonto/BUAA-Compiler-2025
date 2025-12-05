package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMGetElementPtr implements LLVM {
    private String reslabel;
    private String label;
    private String offset;

    public LLVMGetElementPtr(String reslabel, String label, String offset) {
        this.reslabel = reslabel;
        this.label = label;
        this.offset = offset;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(reslabel);
        strb.append(" = getelementptr inbounds i32, i32* ");
        strb.append(label);
        strb.append(", i32 ");
        strb.append(offset);
        strb.append("\n");
    }
}
