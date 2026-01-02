package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSStoreWord;

import java.util.HashSet;

public class LLVMStore implements LLVM {
    private String srclabel;
    private String dstlabel;
    private boolean isPointer;

    public LLVMStore(String srclabel, String dstlabel, boolean isPointer) {
        this.srclabel = srclabel;
        this.dstlabel = dstlabel;
        this.isPointer = isPointer;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("store i32");
        if (isPointer) {
            strb.append("*");
        }
        strb.append(" ");
        strb.append(srclabel);
        strb.append(", i32*");
        if (isPointer) {
            strb.append("*");
        }
        strb.append(" ");
        strb.append(dstlabel);
        strb.append("\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        Register srcreg = mipses.allocRegister(srclabel);
        mipses.loadLabel(srclabel, srcreg, this);
        Register dstreg = mipses.allocRegister(dstlabel);
        mipses.loadLabel(dstlabel, dstreg, this);
        MIPSStoreWord mipsStoreWord = new MIPSStoreWord(srcreg, dstreg, "0", this);
        mipses.addMIPSTextSegment(mipsStoreWord);
    }

    public String getDef() {
        return null;
    }

    public HashSet<String> getUse() {
        HashSet<String> set = new HashSet<String>();
        set.add(srclabel);
        set.add(dstlabel);
        return set;
    }
}
