package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.instruction.MIPSJump;

public class LLVMJump implements LLVM {
    private LLVMLabel label;

    public LLVMJump(LLVMLabel label) {
        this.label = label;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("br label %");
        strb.append(label.getNumber());
        strb.append("\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        String mipslabel = mipses.getNowFunc() + "_" + label.getNumber();
        MIPSJump mipsJump = new MIPSJump(mipslabel, this);
        mipses.addMIPSTextSegment(mipsJump);
    }
}
