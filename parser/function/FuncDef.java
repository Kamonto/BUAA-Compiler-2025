package parser.function;

import lexer.Token;
import llvmgenerator.LLVMTable;
import llvmgenerator.instruction.*;
import parser.block.Block;
import parser.type.FuncType;
import symbolizer.*;

import java.util.ArrayList;

public class FuncDef {
    private FuncType funcType;
    private Token ident;
    private boolean hasFuncFParams;
    private FuncFParams funcFParams;
    private Block block;

    public FuncDef(FuncType funcType, Token ident, boolean hasFuncFParams, FuncFParams funcFParams, Block block) {
        this.funcType = funcType;
        this.ident = ident;
        this.hasFuncFParams = hasFuncFParams;
        this.funcFParams = funcFParams;
        this.block = block;
    }

    public void print(StringBuilder strb) {
        funcType.print(strb);
        strb.append("IDENFR ").append(ident.getContent()).append("\n");
        strb.append("LPARENT (\n");
        if (hasFuncFParams) {
            funcFParams.print(strb);
        }
        strb.append("RPARENT )\n");
        block.print(strb);
        strb.append("<FuncDef>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        symbols.checkDuplicateDeclaration(ident);
        Symbol symbol;
        FuncSymbol funcSymbol;
        if (funcType.isVoid()) {
            symbol = new Symbol(scope, ident.getContent(), SymbolType.VoidFunc);
            funcSymbol = new FuncSymbol(scope, ident.getContent(), false);
        }
        else {
            symbol = new Symbol(scope, ident.getContent(), SymbolType.IntFunc);
            funcSymbol = new FuncSymbol(scope, ident.getContent(), true);
        }
        symbols.addSymbol(symbol);

        scope.forcePush();
        if (hasFuncFParams) {
            funcFParams.symbolize(funcSymbol, symbols, scope);
        }
        scope.forcePop();

        symbols.addFuncSymbol(funcSymbol);
        block.symbolize(!funcType.isVoid(), symbols, scope);
    }

    public void llvmGenerate(SymbolTable symbols, Scope scope, LLVMTable llvms) {
        String label = "@" + ident.getContent();
        Symbol symbol = symbols.getSymbol(scope.getScope(), ident.getContent());
        symbol.setLabel(label);
        FuncSymbol funcSymbol = symbols.getFuncSymbol(scope.getScope(), ident.getContent());
        funcSymbol.setLabel(label);

        scope.forcePush();
        ArrayList<String> paramNames = new ArrayList<String>();
        ArrayList<String> paramLabels = new ArrayList<String>();
        ArrayList<Boolean> isPointers = funcSymbol.getParams();
        int size = isPointers.size();
        for (int i = 0; i < size; i++) {
            paramLabels.add("%" + scope.allocNumber());
        }
        LLVMDefFunc llvmDefFunc = new LLVMDefFunc(label, funcSymbol.hasReturnValue(), paramLabels, isPointers);
        llvms.addLLVM(llvmDefFunc);
        scope.skipLabel();

        if (hasFuncFParams) {
            funcFParams.llvmGenerate(paramNames, symbols, scope, llvms);
            for (int i = 0; i < size; i++) {
                String templabel = "%" + scope.allocNumber();
                Symbol tempsymbol = symbols.getSymbol(scope.getScope(), paramNames.get(i));
                LLVMAllocVar llvmAllocVar = new LLVMAllocVar(templabel, isPointers.get(i));
                llvms.addLLVM(llvmAllocVar);
                LLVMStore llvmStore = new LLVMStore(paramLabels.get(i), templabel, isPointers.get(i));
                llvms.addLLVM(llvmStore);
                if (isPointers.get(i)) {
                    String tempptrlabel = "%" + scope.allocNumber();
                    LLVMLoad llvmLoad = new LLVMLoad(templabel, tempptrlabel, isPointers.get(i));
                    llvms.addLLVM(llvmLoad);
                    tempsymbol.setLabel(tempptrlabel);
                    ArrSymbol temparrSymbol = symbols.getArrSymbol(scope.getScope(), paramNames.get(i));
                    temparrSymbol.setLabel(tempptrlabel);
                }
                else {
                    tempsymbol.setLabel(templabel);
                }
            }
        }
        scope.forcePop();

        block.llvmGenerate(symbols, scope, llvms);
        LLVMDefFuncEnd llvmDefFuncEnd = new LLVMDefFuncEnd();
        llvms.addLLVM(llvmDefFuncEnd);
        llvms.checkLastLabel(!funcType.isVoid());
    }
}
