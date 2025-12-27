package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSBranchIfNotEqualZero;
import mipsgenerator.instruction.MIPSJump;

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

    public void mipsGenerate(MIPSTable mipses) {
        Register reg = mipses.allocRegister(bitlabel);
        mipses.loadLabel(bitlabel, reg, this);
        String mipslabel1 = mipses.getNowFunc() + "_" + label1.getNumber();
        MIPSBranchIfNotEqualZero mipsBranchIfNotEqualZero = new MIPSBranchIfNotEqualZero(reg, mipslabel1, this);
        mipses.addMIPSTextSegment(mipsBranchIfNotEqualZero);
        String mipslabel2 = mipses.getNowFunc() + "_" + label2.getNumber();
        MIPSJump mipsJump = new MIPSJump(mipslabel2, this);
        mipses.addMIPSTextSegment(mipsJump);
    }
}
