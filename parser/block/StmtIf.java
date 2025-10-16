package parser.block;

public class StmtIf implements Stmt {
    private Cond cond;
    private Stmt stmt;
    private boolean hasElse;
    private Stmt anostmt;

    public StmtIf(Cond cond, Stmt stmt, boolean hasElse, Stmt anostmt) {
        this.cond = cond;
        this.stmt = stmt;
        this.hasElse = hasElse;
        this.anostmt = anostmt;
    }

    public void print(StringBuilder strb) {
        strb.append("IFTK if\n");
        strb.append("LPARENT (\n");
        cond.print(strb);
        strb.append("RPARENT )\n");
        stmt.print(strb);
        if (hasElse) {
            strb.append("ELSETK else\n");
            anostmt.print(strb);
        }
        strb.append("<Stmt>\n");
    }
}
