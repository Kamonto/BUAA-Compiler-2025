package parser.block;

public class StmtBlock implements Stmt {
    private Block block;

    public StmtBlock(Block block) {
        this.block = block;
    }

    public void print(StringBuilder strb) {
        block.print(strb);
        strb.append("<Stmt>\n");
    }
}
