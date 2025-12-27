package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.instruction.MIPSPutStr;

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

    public void mipsGenerate(MIPSTable mipses) {
        String mipslabel = label.substring(1);
        MIPSPutStr mipsPutStr = new MIPSPutStr(mipslabel, this);
        mipses.addMIPSTextSegment(mipsPutStr);
    }
}
