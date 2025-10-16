package parser.function;

import parser.block.Block;
import parser.type.FuncType;

public class FuncDef {
    private FuncType funcType;
    private String ident;
    private boolean hasFuncFParams;
    private FuncFParams funcFParams;
    private Block block;

    public FuncDef(FuncType funcType, String ident, boolean hasFuncFParams, FuncFParams funcFParams, Block block) {
        this.funcType = funcType;
        this.ident = ident;
        this.hasFuncFParams = hasFuncFParams;
        this.funcFParams = funcFParams;
        this.block = block;
    }

    public void print(StringBuilder strb) {
        funcType.print(strb);
        strb.append("IDENFR ").append(ident).append("\n");
        strb.append("LPARENT (\n");
        if (hasFuncFParams) {
            funcFParams.print(strb);
        }
        strb.append("RPARENT )\n");
        block.print(strb);
        strb.append("<FuncDef>\n");
    }
}
