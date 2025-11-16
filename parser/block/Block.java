package parser.block;

import lexer.Token;
import symbolizer.Scope;
import symbolizer.SymbolTable;

import java.util.ArrayList;

public class Block {
    private Token rBraceToken;
    private ArrayList<BlockItem> blockItems;

    public Block(Token rBraceToken, ArrayList<BlockItem> blockItems) {
        this.rBraceToken = rBraceToken;
        this.blockItems = blockItems;
    }

    public void print(StringBuilder strb) {
        strb.append("LBRACE {\n");
        for (BlockItem blockItem : blockItems) {
            blockItem.print(strb);
        }
        strb.append("RBRACE }\n");
        strb.append("<Block>\n");
    }

    public void symbolize(boolean nonVoidFuncBlock, SymbolTable symbols, Scope scope) {
        scope.push();
        for (BlockItem blockItem : blockItems) {
            blockItem.symbolize(symbols, scope);
        }
        if (nonVoidFuncBlock) {
            symbols.checkMissReturnInNonVoidFunc(blockItems, rBraceToken);
        }
        scope.pop();
    }
}
