package parser.block;

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
}
