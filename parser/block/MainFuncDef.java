package parser.block;

import symbolizer.FuncSymbol;
import symbolizer.Scope;
import symbolizer.SymbolTable;

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
}
