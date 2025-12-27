package parser.block;

import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.LLVMDefFunc;
import llvmgenerator.instruction.LLVMDefFuncEnd;
import symbolizer.FuncSymbol;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class MainFuncDef {
    private Block block;

    public MainFuncDef(Block block) {
        this.block = block;
    }

    public void print(StringBuilder strb) {
        strb.append("INTTK int\n");
        strb.append("MAINTK main\n");
        strb.append("LPARENT (\n");
        strb.append("RPARENT )\n");
        block.print(strb);
        strb.append("<MainFuncDef>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        FuncSymbol funcSymbol;
        funcSymbol = new FuncSymbol(scope, "main", true);
        symbols.addFuncSymbol(funcSymbol);
        block.symbolize(true, symbols, scope);
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        String label = "@main";
        FuncSymbol funcSymbol = symbols.getFuncSymbol(scope.getScope(), "main");
        funcSymbol.setLabel(label);
        LLVMDefFunc llvmDefFunc = new LLVMDefFunc(label, true, new ArrayList<String>(), new ArrayList<Boolean>());
        llvms.addLLVM(llvmDefFunc);
        scope.forcePush();
        scope.skipLabel();
        scope.forcePop();
        block.llvmGenerate(symbols, scope, llvms);
        LLVMDefFuncEnd llvmDefFuncEnd = new LLVMDefFuncEnd();
        llvms.addLLVM(llvmDefFuncEnd);
        llvms.checkLastLabel(true);
    }
}
