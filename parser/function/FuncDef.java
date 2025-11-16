package parser.function;

import lexer.Token;
import parser.block.Block;
import parser.type.FuncType;
import symbolizer.*;

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
        if (hasFuncFParams) {
            funcFParams.symbolize(funcSymbol, symbols, scope);
        }
        symbols.addFuncSymbol(funcSymbol);
        block.symbolize(!funcType.isVoid(), symbols, scope);
    }
}
