package parser.block;

import java.util.ArrayList;

public class Block {
    private ArrayList<BlockItem> blockItems;

    public Block(ArrayList<BlockItem> blockItems) {
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
}
