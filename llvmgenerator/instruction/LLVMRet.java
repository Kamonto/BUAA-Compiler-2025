package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSJumpRegister;
import mipsgenerator.instruction.MIPSMove;
import mipsgenerator.instruction.MIPSOver;

import java.util.HashSet;

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
                Register reg = mipses.allocRegister(label);
                mipses.loadLabel(label, reg, this);
                MIPSMove mipsMove = new MIPSMove(reg, Register.$v0, this);
                mipses.addMIPSTextSegment(mipsMove);
            }
            MIPSJumpRegister mipsJumpRegister = new MIPSJumpRegister(Register.$ra, this);
            mipses.addMIPSTextSegment(mipsJumpRegister);
        }
    }

    public String getDef() {
        return null;
    }

    public HashSet<String> getUse() {
        HashSet<String> set = new HashSet<String>();
        if (hasReturnValue) {
            set.add(label);
        }
        return set;
    }
}
