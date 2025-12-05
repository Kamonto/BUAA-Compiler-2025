package parser.block;

import llvmgenerator.LLVMTable;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public interface BlockItem {
    public void print(StringBuilder strb);
    public void symbolize(SymbolTable symbols, Scope scope);
    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms);
}
