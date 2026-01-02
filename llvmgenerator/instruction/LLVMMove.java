package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSMove;

public class LLVMMove implements LLVM {
    private String srclabel;
    private String dstlabel;

    public LLVMMove(String srclabel, String dstlabel) {
        this.srclabel = srclabel;
        this.dstlabel = dstlabel;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(dstlabel);
        strb.append(" = ");
        strb.append(srclabel);
        strb.append("\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        Register srcreg = mipses.allocRegister(srclabel);
        mipses.loadLabel(srclabel, srcreg, this);
        Register dstreg = mipses.allocRegister(dstlabel);
        MIPSMove mipsMove = new MIPSMove(srcreg, dstreg, this);
        mipses.addMIPSTextSegment(mipsMove);
        mipses.storeLabel(dstlabel, dstreg, this);
    }
}
