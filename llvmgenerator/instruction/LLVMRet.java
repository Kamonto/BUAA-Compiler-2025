package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSJumpRegister;
import mipsgenerator.instruction.MIPSOver;

public class LLVMRet implements LLVM {
    boolean hasReturnValue;
    String label;

    public LLVMRet(boolean hasReturnValue, String label) {
        this.hasReturnValue = hasReturnValue;
        this.label = label;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("ret ");
        if (hasReturnValue) {
            strb.append("i32 ");
            strb.append(label);
        }
        else {
            strb.append("void");
        }
        strb.append("\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        if (mipses.getNowFunc().equals("main")) {
            MIPSOver mipsOver = new MIPSOver(this);
            mipses.addMIPSTextSegment(mipsOver);
        }
        else {
            if (hasReturnValue) {
                mipses.loadLabel(label, Register.$v0, this);
            }
            MIPSJumpRegister mipsJumpRegister = new MIPSJumpRegister(Register.$ra, this);
            mipses.addMIPSTextSegment(mipsJumpRegister);
        }
    }
}
