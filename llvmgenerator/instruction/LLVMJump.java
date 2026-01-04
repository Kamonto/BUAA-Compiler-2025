package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.instruction.MIPSJump;

import java.util.ArrayList;
import java.util.HashSet;

public class LLVMJump implements LLVM {
    private LLVMLabel label;
    private ArrayList<LLVM> llvms;

    public LLVMJump(LLVMLabel label, ArrayList<LLVM> llvms) {
        this.label = label;
        this.llvms = llvms;
    }

    public LLVMLabel getLabel() {
        return label;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append("br label %");
        strb.append(label.getNumber());
        strb.append("\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        int index = llvms.indexOf(this);
        if (index < llvms.size() - 1 && llvms.get(index + 1) instanceof LLVMLabel) {
            LLVMLabel llvmLabel = (LLVMLabel) llvms.get(index + 1);
            if (label == llvmLabel) {
                // do nothing
            }
            else {
                String mipslabel = mipses.getNowFunc() + "_" + label.getNumber();
                MIPSJump mipsJump = new MIPSJump(mipslabel, this);
                mipses.addMIPSTextSegment(mipsJump);
            }
        }
        else {
            String mipslabel = mipses.getNowFunc() + "_" + label.getNumber();
            MIPSJump mipsJump = new MIPSJump(mipslabel, this);
            mipses.addMIPSTextSegment(mipsJump);
        }
    }

    public String getDef() {
        return null;
    }

    public HashSet<String> getUse() {
        HashSet<String> set = new HashSet<String>();
        return set;
    }
}
