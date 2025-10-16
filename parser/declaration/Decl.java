package parser.declaration;

import parser.block.BlockItem;

public interface Decl extends BlockItem {
    public void print(StringBuilder strb);
}
