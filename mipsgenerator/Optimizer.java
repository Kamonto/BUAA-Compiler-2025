package mipsgenerator;

import llvmgenerator.LLVM;
import llvmgenerator.instruction.*;

import java.util.*;

public class Optimizer {
    private ArrayList<LLVM> llvms;
    private ArrayList<Register> pool;
    private ArrayList<String> blockNames;
    private HashMap<String, ArrayList<LLVM>> blocks;
    private HashMap<String, HashSet<String>> in2out;
    private HashMap<String, HashSet<String>> out2in;
    private HashMap<String, HashSet<String>> def;
    private HashMap<String, HashSet<String>> use;
    private HashMap<String, HashSet<String>> in;
    private HashMap<String, HashSet<String>> out;
    private HashMap<String, HashSet<String>> crushMap;
    private HashMap<String, Register> label2regs;
    private HashMap<LLVM, ArrayList<String>> activeLabelsWhenCall;

    public Optimizer(ArrayList<LLVM> llvms) {
        this.llvms = llvms;
        initPool();
        blockNames = new ArrayList<String>();
        blocks = new HashMap<>();
        in2out = new HashMap<>();
        out2in = new HashMap<>();
        def = new HashMap<>();
        use = new HashMap<>();
        in = new HashMap<>();
        out = new HashMap<>();
        crushMap = new HashMap<>();
        label2regs = new HashMap<String, Register>();
        activeLabelsWhenCall = new HashMap<>();
    }

    private void initPool() {
        pool = new ArrayList<Register>(Arrays.asList(Register.$a0, Register.$a1, Register.$a2, Register.$a3,
                Register.$s0, Register.$s1, Register.$s2, Register.$s3, Register.$s4, Register.$s5, Register.$s6, Register.$s7));
    }

    public HashMap<String, Register> getLabel2regs() {
        return label2regs;
    }

    public ArrayList<String> getActiveLabelsWhenCall(LLVM llvm) {
        return activeLabelsWhenCall.get(llvm);
    }

    public void cutBlocks() {
        String blockName = null;
        for (LLVM llvm : llvms) {
            if (llvm instanceof LLVMDefFunc) {
                blockName = "begin";
                blockNames.add(blockName);
                blocks.put(blockName, new ArrayList<LLVM>());
                in2out.put(blockName, new HashSet<String>());
                out2in.put(blockName, new HashSet<String>());
                def.put(blockName, new HashSet<String>());
                use.put(blockName, new HashSet<String>());
                in.put(blockName, new HashSet<String>());
                out.put(blockName, new HashSet<String>());
            }
            else if (llvm instanceof LLVMLabel) {
                blockName = "%" + ((LLVMLabel) llvm).getNumber();
                blockNames.add(blockName);
                blocks.put(blockName, new ArrayList<LLVM>());
                in2out.put(blockName, new HashSet<String>());
                out2in.put(blockName, new HashSet<String>());
                def.put(blockName, new HashSet<String>());
                use.put(blockName, new HashSet<String>());
                in.put(blockName, new HashSet<String>());
                out.put(blockName, new HashSet<String>());
            }
            else {
                if (blockName != null) {
                    blocks.get(blockName).add(llvm);
                }
            }
        }
        for (LLVM llvm : llvms) {
            if (llvm instanceof LLVMDefFunc) {
                blockName = "begin";
            }
            else if (llvm instanceof LLVMLabel) {
                blockName = "%" + ((LLVMLabel) llvm).getNumber();
            }
            else if (llvm instanceof LLVMBranch) {
                String label1 = "%" + ((LLVMBranch) llvm).getLabel1().getNumber();
                out2in.get(blockName).add(label1);
                in2out.get(label1).add(blockName);
                String label2 = "%" + ((LLVMBranch) llvm).getLabel2().getNumber();
                out2in.get(blockName).add(label2);
                in2out.get(label2).add(blockName);
            }
            else if (llvm instanceof LLVMJump) {
                String label = "%" + ((LLVMJump) llvm).getLabel().getNumber();
                out2in.get(blockName).add(label);
                in2out.get(label).add(blockName);
            }
        }
    }

    public void defuse() {
        for (String blockName : blockNames) {
            ArrayList<LLVM> eachBlocks = blocks.get(blockName);
            HashSet<String> eachDef = def.get(blockName);
            HashSet<String> eachUse = use.get(blockName);
            for (LLVM llvm : eachBlocks) {
                String defLabel = filterFuncVar(llvm.getDef());
                HashSet<String> useLabels = filterFuncVar(llvm.getUse());
                for (String useLabel : useLabels) {
                    if (!eachDef.contains(useLabel)) {
                        eachUse.add(useLabel);
                    }
                }
                if (defLabel != null) {
                    if (!eachUse.contains(defLabel)) {
                        eachDef.add(defLabel);
                    }
                }
            }
        }
        ArrayList<String> reversedBlockNames = new ArrayList<String>(blockNames);
        Collections.reverse(reversedBlockNames);
        boolean over;
        do {
            over = true;
            for (String blockName : reversedBlockNames) {
                HashSet<String> eachDef = def.get(blockName);
                HashSet<String> eachUse = use.get(blockName);
                HashSet<String> eachOut = out.get(blockName);
                HashSet<String> nowIn = new HashSet<String>();
                HashSet<String> nowOut = new HashSet<String>();
                for (String block : out2in.get(blockName)) {
                    nowOut.addAll(in.get(block));
                }
                HashSet<String> temp = new HashSet<String>(nowOut);
                temp.removeAll(eachDef);
                nowIn.addAll(eachUse);
                nowIn.addAll(temp);
                if (!eachOut.equals(nowOut)) {
                    over = false;
                }
                in.put(blockName, nowIn);
                out.put(blockName, nowOut);
            }
        } while (!over);
    }

    public void allocreg() {
        for (String beginLabel : in.get("begin")) {
            if (!crushMap.containsKey(beginLabel)) {
                crushMap.put(beginLabel, new HashSet<String>());
            }
        }
        for (String beginLabel1 : in.get("begin")) {
            for (String beginLabel2 : in.get("begin")) {
                if (beginLabel1 != beginLabel2) {
                    crushMap.get(beginLabel1).add(beginLabel2);
                }
            }
        }
        for (String blockName : blockNames) {
            ArrayList<LLVM> reversedEachBlocks = blocks.get(blockName);
            Collections.reverse(reversedEachBlocks);
            HashSet<String> nowOut = new HashSet<String>(out.get(blockName));
            for (String label : nowOut) {
                if (!crushMap.containsKey(label)) {
                    crushMap.put(label, new HashSet<String>());
                }
            }
            for (LLVM llvm : reversedEachBlocks) {
                String defLabel = filterFuncVar(llvm.getDef());
                HashSet<String> useLabels = filterFuncVar(llvm.getUse());
                if (defLabel != null) {
                    if (!crushMap.containsKey(defLabel)) {
                        crushMap.put(defLabel, new HashSet<String>());
                    }
                }
                for (String label : useLabels) {
                    if (!crushMap.containsKey(label)) {
                        crushMap.put(label, new HashSet<String>());
                    }
                }
                if (defLabel != null) {
                    nowOut.remove(defLabel);
                    for (String label : nowOut) {
                        crushMap.get(defLabel).add(label);
                        crushMap.get(label).add(defLabel);
                    }
                }
                if (llvm instanceof LLVMCall) {
                    activeLabelsWhenCall.put(llvm, new ArrayList<String>(nowOut));
                }
                nowOut.addAll(useLabels);
            }
        }
        HashMap<String, HashSet<String>> copiedCrushMap = new HashMap<>();
        for (String key : crushMap.keySet()) {
            HashSet<String> value = crushMap.get(key);
            HashSet<String> copiedValue = new HashSet<String>(value);
            copiedCrushMap.put(key, copiedValue);
        }
        while (!copiedCrushMap.isEmpty()) {
            int minDegree = copiedCrushMap.size() + 1;
            String minLabel = null;
            int maxDegree = -1;
            String maxLabel = null;
            for (String key : copiedCrushMap.keySet()) {
                HashSet<String> value = copiedCrushMap.get(key);
                if (value.size() < minDegree) {
                    minDegree = value.size();
                    minLabel = key;
                }
                if (value.size() > maxDegree) {
                    maxDegree = value.size();
                    maxLabel = key;
                }
            }
            if (minDegree < pool.size()) {
                HashSet<Register> allocedregs = new HashSet<Register>();
                for (String item : crushMap.get(minLabel)) {
                    if (label2regs.containsKey(item)) {
                        allocedregs.add(label2regs.get(item));
                    }
                }
                for (Register reg : pool) {
                    if (!allocedregs.contains(reg)) {
                        label2regs.put(minLabel, reg);
                    }
                }
                for (String item : copiedCrushMap.get(minLabel)) {
                    copiedCrushMap.get(item).remove(minLabel);
                }
                copiedCrushMap.remove(minLabel);
            }
            else {
                for (String item : copiedCrushMap.get(maxLabel)) {
                    copiedCrushMap.get(item).remove(maxLabel);
                }
                copiedCrushMap.remove(maxLabel);
            }
        }
    }

    private String filterFuncVar(String str) {
        if (str != null && str.charAt(0) == '%') {
            return str;
        }
        else {
            return null;
        }
    }

    private HashSet<String> filterFuncVar(HashSet<String> set) {
        set.removeIf(str -> str.charAt(0) != '%');
        return set;
    }
}
