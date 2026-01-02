package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.instruction.MIPSLabel;

import java.util.HashSet;

public class LLVMLabel implements LLVM {
    private int number;

    public LLVMLabel() {
        number = -1;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void print(StringBuilder strb) {
        strb.append(number);
        strb.append(":\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        String mipslabel = mipses.getNowFunc() + "_" + number;
        MIPSLabel mipsLabel = new MIPSLabel(mipslabel, this);
        mipses.addMIPSTextSegment(mipsLabel);
    }

    public String getDef() {
        return null;
    }

    public HashSet<String> getUse() {
        HashSet<String> set = new HashSet<String>();
        return set;
    }
}
