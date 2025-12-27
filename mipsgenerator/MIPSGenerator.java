package mipsgenerator;

import llvmgenerator.LLVMTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MIPSGenerator {
    private final LLVMTable llvms;
    private MIPSTable mipses;

    public MIPSGenerator(LLVMTable llvms) {
        this.llvms = llvms;
        this.mipses = new MIPSTable();
    }

    public MIPSTable mipsGenerate() {
        llvms.mipsGenerate(mipses);
        return mipses;
    }

    public void printMIPSes() throws IOException {
        Path path = Paths.get("mips.txt");
        Files.write(path, mipses.print().toString().getBytes());
    }
}
