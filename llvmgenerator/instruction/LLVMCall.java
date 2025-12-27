package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.Register;
import mipsgenerator.instruction.MIPSAddi;
import mipsgenerator.instruction.MIPSJumpAndLink;

import java.util.ArrayList;

public class LLVMCall implements LLVM {
    private String reslabel;
    private String label;
    private boolean hasReturnValue;
    private ArrayList<String> paramLabels;
    private ArrayList<Boolean> isPointers;

    public LLVMCall(String reslabel, String label, boolean hasReturnValue, ArrayList<String> paramLabels, ArrayList<Boolean> isPointers) {
        this.reslabel = reslabel;
        this.label = label;
        this.hasReturnValue = hasReturnValue;
        this.paramLabels = paramLabels;
        this.isPointers = isPointers;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        if (hasReturnValue) {
            strb.append(reslabel);
            strb.append(" = ");
        }
        strb.append("call ");
        if (hasReturnValue) {
            strb.append("i32 ");
        }
        else {
            strb.append("void ");
        }
        strb.append(label);
        strb.append("(");
        int size = paramLabels.size();
        for (int i = 0; i < size; i++) {
            strb.append("i32");
            if (isPointers.get(i)) {
                strb.append("*");
            }
            strb.append(" ");
            strb.append(paramLabels.get(i));
            if (i < size - 1) {
                strb.append(", ");
            }
        }
        strb.append(")\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        int callCount = mipses.getCallCount();
        ArrayList<Register> usingRegs = new ArrayList<Register>();
        usingRegs.add(Register.$sp);
        usingRegs.add(Register.$ra);
        for (int i = 0; i < usingRegs.size(); i++) {
            Register usingReg = usingRegs.get(i);
            String label = "usingReg" + callCount + "_" + i;
            mipses.storeLabel(label, usingReg, this);
        }
        for (int i = 0; i < paramLabels.size(); i++) {
            String paramLabel = paramLabels.get(i);
            Register paramreg = mipses.allocRegister(paramLabel);
            mipses.loadLabel(paramLabel, paramreg, this);
            String label = "param" + callCount + "_" + i;
            mipses.storeLabel(label, paramreg, this);
        }
        int fpoffset = mipses.getNowfpoffset();
        int spoffset = mipses.getNowspoffset() + paramLabels.size() * 4;
        MIPSAddi fpmipsAddi = new MIPSAddi(Register.$fp, Register.$fp, fpoffset, this);
        mipses.addMIPSTextSegment(fpmipsAddi);
        MIPSAddi spmipsAddi = new MIPSAddi(Register.$sp, Register.$sp, spoffset, this);
        mipses.addMIPSTextSegment(spmipsAddi);
        String mipslabel = label.substring(1);
        MIPSJumpAndLink mipsJumpAndLink = new MIPSJumpAndLink(mipslabel, this);
        mipses.addMIPSTextSegment(mipsJumpAndLink);
        MIPSAddi anofpmipsAddi = new MIPSAddi(Register.$fp, Register.$fp, -fpoffset, this);
        mipses.addMIPSTextSegment(anofpmipsAddi);
        MIPSAddi anospmipsAddi = new MIPSAddi(Register.$sp, Register.$sp, -spoffset, this);
        mipses.addMIPSTextSegment(anospmipsAddi);
        for (int i = 0; i < usingRegs.size(); i++) {
            Register usingReg = usingRegs.get(i);
            String label = "usingReg" + callCount + "_" + i;
            mipses.loadLabel(label, usingReg, this);
        }
        if (hasReturnValue) {
            mipses.storeLabel(reslabel, Register.$v0, this);
        }
        mipses.updateCallCount();
    }
}
