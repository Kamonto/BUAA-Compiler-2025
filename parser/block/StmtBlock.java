package parser.block;

import symbolizer.Scope;
import symbolizer.SymbolTable;

public class StmtBlock implements Stmt {
    private Block block;

    public StmtBlock(Block block) {
        this.block = block;
    }

    public void print(StringBuilder strb) {
        block.print(strb);
        strb.append("<Stmt>\n");
    }

    public void symbolize(SymbolTable symbols, Scope scope) {
        block.symbolize(false, symbols, scope);
    }
}
