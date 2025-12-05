package llvmgenerator.instruction;

import llvmgenerator.LLVM;

public class LLVMPutStr implements LLVM {
    private int size;
    private String label;

    public LLVMPutStr(int size, String label) {
        this.size = size;
        this.label = label;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("call void @putstr(i8* getelementptr inbounds ([");
        strb.append(size);
        strb.append(" x i8], [");
        strb.append(size);
        strb.append(" x i8]* ");
        strb.append(label);
        strb.append(", i64 0, i64 0))\n");
    }
}
