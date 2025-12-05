package parser.declaration;

import llvmgenerator.LLVMTable;
import parser.block.BlockItem;
import symbolizer.Scope;
import symbolizer.SymbolTable;

public interface Decl extends BlockItem {
    public void print(StringBuilder strb);
    public void symbolize(SymbolTable symbols, Scope scope);
    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms);
}
