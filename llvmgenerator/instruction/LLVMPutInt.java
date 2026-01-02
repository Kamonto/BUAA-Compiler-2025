package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSPutInt;

import java.util.HashSet;

public class LLVMPutInt implements LLVM {
    private String label;

    public LLVMPutInt(String label) {
        this.label = label;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("call void @putint(i32 ");
        strb.append(label);
        strb.append(")\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        Register srcreg = mipses.allocRegister(label);
        mipses.loadLabel(label, srcreg, this);
        MIPSPutInt mipsPutInt = new MIPSPutInt(srcreg, this);
        mipses.addMIPSTextSegment(mipsPutInt);
    }

    public String getDef() {
        return null;
    }

    public HashSet<String> getUse() {
        HashSet<String> set = new HashSet<String>();
        set.add(label);
        return set;
    }
}
