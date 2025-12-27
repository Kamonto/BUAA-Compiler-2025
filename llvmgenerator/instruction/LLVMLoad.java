package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSLoadWord;

public class LLVMLoad implements LLVM {
    private String srclabel;
    private String dstlabel;
    private boolean isPointer;

    public LLVMLoad(String srclabel, String dstlabel, boolean isPointer) {
        this.srclabel = srclabel;
        this.dstlabel = dstlabel;
        this.isPointer = isPointer;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(dstlabel);
        strb.append(" = load i32");
        if (isPointer) {
            strb.append("*");
        }
        strb.append(", i32*");
        if (isPointer) {
            strb.append("*");
        }
        strb.append(" ");
        strb.append(srclabel);
        strb.append("\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        Register srcreg = mipses.allocRegister(srclabel);
        mipses.loadLabel(srclabel, srcreg, this);
        Register dstreg = mipses.allocRegister(dstlabel);
        MIPSLoadWord mipsLoadWord = new MIPSLoadWord(dstreg, srcreg, 0, this);
        mipses.addMIPSTextSegment(mipsLoadWord);
        mipses.storeLabel(dstlabel, dstreg, this);
    }
}
