package llvmgenerator.instruction;

import llvmgenerator.LLVM;

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
}
