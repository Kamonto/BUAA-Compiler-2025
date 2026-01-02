package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.instruction.MIPSLabel;

import java.util.ArrayList;
import java.util.HashSet;

public class LLVMDefFunc implements LLVM {
    private String label;
    private boolean hasReturnValue;
    private ArrayList<String> paramLabels;
    private ArrayList<Boolean> isPointers;

    public LLVMDefFunc(String label, boolean hasReturnValue, ArrayList<String> paramLabels, ArrayList<Boolean> isPointers) {
        this.label = label;
        this.hasReturnValue = hasReturnValue;
        this.paramLabels = paramLabels;
        this.isPointers = isPointers;
    }

    public void print(StringBuilder strb) {
//        strb.append("\n");
        strb.append("define dso_local ");
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
        strb.append(") {\n");
    }

    public void mipsGenerate(MIPSTable mipses) {
        String mipslabel = label.substring(1);
        MIPSLabel mipsLabel = new MIPSLabel(mipslabel, this);
        mipses.addMIPSTextSegment(mipsLabel);
        mipses.newFunc(label, paramLabels, this);
    }

    public String getDef() {
        return null;
    }

    public HashSet<String> getUse() {
        HashSet<String> set = new HashSet<String>();
        return set;
    }
}
