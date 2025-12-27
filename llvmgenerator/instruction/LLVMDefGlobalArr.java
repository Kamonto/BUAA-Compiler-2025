package llvmgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPSTable;
import mipsgenerator.instruction.MIPSDefGlobalArr;

import java.util.ArrayList;

public class LLVMDefGlobalArr implements LLVM {
    private String label;
    private int size;
    private ArrayList<Integer> values;

    public LLVMDefGlobalArr(String label, int size, ArrayList<Integer> values) {
        this.label = label;
        this.size = size;
        this.values = values;
    }

    public void print(StringBuilder strb) {
        boolean allzero = true;
        for (int value : values) {
            if (value != 0) {
                allzero = false;
                break;
            }
        }
        if (allzero) {
            strb.append(label);
            strb.append(" = dso_local global [");
            strb.append(size);
            strb.append(" x i32] zeroinitializer\n");
        }
        else {
            strb.append(label);
            strb.append(" = dso_local global [");
            strb.append(size);
            strb.append(" x i32] [");
            for (int i = 0; i < size; i++) {
                strb.append("i32 ");
                strb.append(values.get(i));
                if (i < size - 1) {
                    strb.append(", ");
                }
            }
            strb.append("]\n");
        }
    }

    public void mipsGenerate(MIPSTable mipses) {
        String mipslabel = label.substring(1);
        MIPSDefGlobalArr mipsDefGlobalArr = new MIPSDefGlobalArr(mipslabel, size, values, this);
        mipses.addMIPSDataSegment(mipsDefGlobalArr);
    }
}
