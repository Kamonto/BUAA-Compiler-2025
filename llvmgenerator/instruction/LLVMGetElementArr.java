package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMGetElementArr implements LLVM {
    private String reslabel;
    private int size;
    private String label;
    private String offset;

    public LLVMGetElementArr(String reslabel, int size, String label, String offset) {
        this.reslabel = reslabel;
        this.size = size;
        this.label = label;
        this.offset = offset;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(reslabel);
        strb.append(" = getelementptr inbounds [");
        strb.append(size);
        strb.append(" x i32], [");
        strb.append(size);
        strb.append(" x i32]* ");
        strb.append(label);
        strb.append(", i32 0, i32 ");
        strb.append(offset);
        strb.append("\n");
    }
}
