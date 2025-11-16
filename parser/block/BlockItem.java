package parser.block;

import symbolizer.Scope;
import symbolizer.SymbolTable;

public interface BlockItem {
    public void print(StringBuilder strb);
    public void symbolize(SymbolTable symbols, Scope scope);
}
