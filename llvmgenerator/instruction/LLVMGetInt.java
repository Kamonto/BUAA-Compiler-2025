package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSGetInt;

public class LLVMGetInt implements LLVM {
    private String label;

    public LLVMGetInt(String label) {
        this.label = label;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(label);
        strb.append(" = call i32 @getint()\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        Register dstreg = mipses.allocRegister(label);
        MIPSGetInt mipsGetInt = new MIPSGetInt(dstreg, this);
        mipses.addMIPSTextSegment(mipsGetInt);
        mipses.storeLabel(label, dstreg, this);
    }
}
