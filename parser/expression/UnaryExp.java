package parser.expression;

import java.util.ArrayList;

public class UnaryExp {
    private ArrayList<UnaryOp> unaryOps;
    private boolean isPrimaryExp;
    private PrimaryExp primaryExp;
    private String ident;
    private boolean hasFuncRParams;
    private FuncRParams funcRParams;

    public UnaryExp(ArrayList<UnaryOp> unaryOps, boolean isPrimaryExp, PrimaryExp primaryExp,
                    String ident, boolean hasFuncRParams, FuncRParams funcRParams) {
        this.unaryOps = unaryOps;
        this.isPrimaryExp = isPrimaryExp;
        this.primaryExp = primaryExp;
        this.ident = ident;
        this.hasFuncRParams = hasFuncRParams;
        this.funcRParams = funcRParams;
    }

    public void print(StringBuilder strb) {
        for (UnaryOp unaryOp : unaryOps) {
            unaryOp.print(strb);
        }
        if (isPrimaryExp) {
            primaryExp.print(strb);
        }
        else {
            strb.append("IDENFR ").append(ident).append("\n");
            strb.append("LPARENT (\n");
            if (hasFuncRParams) {
                funcRParams.print(strb);
            }
            strb.append("RPARENT )\n");
        }
        strb.append("<UnaryExp>\n");
        for (UnaryOp unaryOp : unaryOps) {
            strb.append("<UnaryExp>\n");
        }
    }
}
