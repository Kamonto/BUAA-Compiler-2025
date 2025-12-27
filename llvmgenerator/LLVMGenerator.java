package llvmgenerator;

import parser.CompUnit;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LLVMGenerator {
    private final CompUnit compUnit;
    private SymbolTable symbols;
    private Scope scope;
    private LLVMTable llvms;

    public LLVMGenerator(CompUnit compUnit, SymbolTable symbols) {
        this.compUnit = compUnit;
        this.symbols = symbols;
        this.scope = new Scope();
        this.llvms = new LLVMTable();
    }

    public LLVMTable llvmGenerate() {
        compUnit.llvmGenerate(symbols, scope, llvms);
        llvms.mergeLLVM();
        return llvms;
    }

    public void printLLVMs() throws IOException {
        Path path = Paths.get("llvm_ir.txt");
        Files.write(path, llvms.print().toString().getBytes());
    }
}
