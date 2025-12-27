package mipsgenerator.instruction;

import llvmgenerator.LLVM;
import mipsgenerator.MIPS;

import java.util.ArrayList;

public class MIPSDefGlobalArr implements MIPS {
    private String mipslabel;
    private int size;
    private ArrayList<Integer> values;
    private LLVM reference;

    public MIPSDefGlobalArr(String mipslabel, int size, ArrayList<Integer> values, LLVM reference) {
        this.mipslabel = mipslabel;
        this.size = size;
        this.values = values;
        this.reference = reference;
    }

    public void print(StringBuilder strb) {
        strb.append("    ");
        strb.append(mipslabel);
        strb.append(": .word ");
        for (int i = 0; i < size; i++) {
            if (i < size - 1) {
                strb.append(values.get(i));
                strb.append(", ");
            }
            else {
                strb.append(values.get(i));
                strb.append("   #");
                reference.print(strb);
            }
        }
    }
}
