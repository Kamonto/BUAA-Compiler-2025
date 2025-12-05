package parser;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMImport;
import parser.block.MainFuncDef;
import parser.declaration.Decl;
import parser.function.FuncDef;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class CompUnit {
    private ArrayList<Decl> decls;
    private ArrayList<FuncDef> funcDefs;
    private MainFuncDef mainFuncDef;

    public CompUnit(ArrayList<Decl> decls, ArrayList<FuncDef> funcDefs, MainFuncDef mainFuncDef) {
        this.decls = decls;
        this.funcDefs = funcDefs;
        this.mainFuncDef = mainFuncDef;
    }

    public void print(StringBuilder strb) {
        for (Decl decl : decls) {
            decl.print(strb);
        }
        for (FuncDef funcDef : funcDefs) {
            funcDef.print(strb);
        }
        mainFuncDef.print(strb);
        strb.append("<CompUnit>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        for (Decl decl : decls) {
            decl.symbolize(symbols, scope);
        }
        for (FuncDef funcDef : funcDefs) {
            funcDef.symbolize(symbols, scope);
        }
        mainFuncDef.symbolize(symbols, scope);
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        LLVMImport llvmImport = new LLVMImport();
        llvms.addLLVM(llvmImport);
        for (Decl decl : decls) {
            decl.llvmGenerate(symbols, scope, llvms);
        }
        for (FuncDef funcDef : funcDefs) {
            funcDef.llvmGenerate(symbols, scope, llvms);
        }
        mainFuncDef.llvmGenerate(symbols, scope, llvms);
    }
}
