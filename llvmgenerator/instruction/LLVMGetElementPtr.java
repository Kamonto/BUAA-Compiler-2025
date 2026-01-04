package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSAdd;
import mipsgenerator.instruction.MIPSAddi;
import mipsgenerator.instruction.MIPSShiftLeftLogical;

import java.util.HashSet;

public class LLVMGetElementPtr implements LLVM {
    private String reslabel;
    private String label;
    private String offset;

    public LLVMGetElementPtr(String reslabel, String label, String offset) {
        this.reslabel = reslabel;
        this.label = label;
        this.offset = offset;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(reslabel);
        strb.append(" = getelementptr inbounds i32, i32* ");
        strb.append(label);
        strb.append(", i32 ");
        strb.append(offset);
        strb.append("\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        Register reg = mipses.allocRegister(label);
        mipses.loadLabel(label, reg, this);
        if (offset.matches("-?\\d+")) {
            Register resreg = mipses.allocRegister(reslabel);
            MIPSAddi mipsAddi = new MIPSAddi(resreg, reg, Integer.parseInt(offset) * 4, this);
            mipses.addMIPSTextSegment(mipsAddi);
            mipses.storeLabel(reslabel, resreg, this);
        }
        else {
            Register offsetreg = mipses.allocRegister(offset);
            mipses.loadLabel(offset, offsetreg, this);
            Register tempreg = mipses.allocTempRegister();
            MIPSShiftLeftLogical mipsShiftLeftLogical = new MIPSShiftLeftLogical(tempreg, offsetreg, 2, this);
            mipses.addMIPSTextSegment(mipsShiftLeftLogical);
            Register resreg = mipses.allocRegister(reslabel);
            MIPSAdd mipsAdd = new MIPSAdd(resreg, reg, tempreg, this);
            mipses.addMIPSTextSegment(mipsAdd);
            mipses.storeLabel(reslabel, resreg, this);
        }
    }

    public String getDef() {
        return reslabel;
    }

    public HashSet<String> getUse() {
        HashSet<String> set = new HashSet<String>();
        set.add(label);
        set.add(offset);
        return set;
    }
}
