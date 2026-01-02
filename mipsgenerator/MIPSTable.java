package mipsgenerator;

import llvmgenerator.LLVM;
import mipsgenerator.instruction.MIPSLoadAddr;
import mipsgenerator.instruction.MIPSLoadImm;
import mipsgenerator.instruction.MIPSLoadWord;
import mipsgenerator.instruction.MIPSStoreWord;

import java.util.ArrayList;
import java.util.HashMap;

public class MIPSTable {
    private ArrayList<MIPS> mipsDataSegments;
    private ArrayList<MIPS> mipsTextSegments;
    private HashMap<String, Register> regs;
    private int nowfpoffset;
    private HashMap<String, Integer> spoffsets;
    private int nowspoffset;
    private String nowFunc;
    private int callCount;
    private ArrayList<Register> pool;
    private int poolindex;

    public MIPSTable() {
        mipsDataSegments = new ArrayList<MIPS>();
        mipsTextSegments = new ArrayList<MIPS>();
        regs = new HashMap<String, Register>();
        nowfpoffset = 0;
        spoffsets = new HashMap<String, Integer>();
        nowspoffset = 0;
        nowFunc = null;
        callCount = 0;
        pool = new ArrayList<Register>();
        pool.add(Register.$t0);
        pool.add(Register.$t1);
        pool.add(Register.$t2);
        pool.add(Register.$t3);
        pool.add(Register.$t4);
        pool.add(Register.$t5);
        pool.add(Register.$t6);
        pool.add(Register.$t7);
        poolindex = 0;
    }

    public int getNowfpoffset() {
        return nowfpoffset;
    }

    public int getNowspoffset() {
        return nowspoffset;
    }

    public String getNowFunc() {
        return nowFunc;
    }

    public int getCallCount() {
        return callCount;
    }

    public void updateCallCount() {
        callCount++;
    }

    public void addMIPSDataSegment(MIPS mips) {
        mipsDataSegments.add(mips);
    }

    public void addMIPSTextSegment(MIPS mips) {
        mipsTextSegments.add(mips);
    }

    public int allocHeapSpace(int size) {
        int oldfpoffset = nowfpoffset;
        nowfpoffset += size * 4;
        return oldfpoffset;
    }

    private void allocStackSpace(String label) {
        nowspoffset -= 4;
        spoffsets.put(label, nowspoffset);
    }

    private int getspoffset(String label) {
        if (spoffsets.containsKey(label)) {
            return spoffsets.get(label);
        }
        return 114514;
    }

    public Register allocRegister(String label) {
        Register reg = pool.get(poolindex);
        poolindex = (poolindex + 1) % pool.size();
        return reg;
    }

    public void loadLabel(String label, Register reg, LLVM reference) {
        char c = label.charAt(0);
        if (c == '@') {
            String mipslabel = label.substring(1);
            MIPSLoadWord mipsLoadWord = new MIPSLoadWord(reg, Register.$zero, mipslabel, reference);
            mipsTextSegments.add(mipsLoadWord);
        }
        else if (Character.isDigit(c)) {
            int imm = Integer.parseInt(label);
            MIPSLoadImm mipsLoadImm = new MIPSLoadImm(reg, imm, reference);
            mipsTextSegments.add(mipsLoadImm);
        }
        else {
            int spoffset = getspoffset(label);
            MIPSLoadWord mipsLoadWord = new MIPSLoadWord(reg, Register.$sp, Integer.toString(spoffset), reference);
            mipsTextSegments.add(mipsLoadWord);
        }
    }

    public void storeLabel(String label, Register reg, LLVM reference) {
        if (!spoffsets.containsKey(label)) {
            allocStackSpace(label);
        }
        char c = label.charAt(0);
        if (c == '@') {
            String mipslabel = label.substring(1);
            MIPSStoreWord mipsStoreWord = new MIPSStoreWord(reg, Register.$zero, mipslabel, reference);
            mipsTextSegments.add(mipsStoreWord);
        }
        else {
            int spoffset = getspoffset(label);
            MIPSStoreWord mipsStoreWord = new MIPSStoreWord(reg, Register.$sp, Integer.toString(spoffset), reference);
            mipsTextSegments.add(mipsStoreWord);
        }
    }

    public void newFunc(String funcLabel, ArrayList<String> paramLabels) {
        nowFunc = funcLabel.substring(1);
        spoffsets.clear();
        nowfpoffset = 0;
        nowspoffset = 0;
        for (String paramLabel : paramLabels) {
            allocStackSpace(paramLabel);
        }
    }

    public StringBuilder print() {
        StringBuilder strb = new StringBuilder();
        strb.append(".data\n");
        for (MIPS mips : mipsDataSegments) {
            mips.print(strb);
        }
        strb.append("    align: .align 2\n");
        strb.append("\n.text\n");
        strb.append("    la $fp, align\n");
        strb.append("    j main\n");
        for (MIPS mips : mipsTextSegments) {
            mips.print(strb);
        }
        return strb;
    }
}
