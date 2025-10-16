package parser.block;

public class StmtFor implements Stmt {
    private boolean hasFormerForStmt;
    private ForStmt forStmt;
    private boolean hasCond;
    private Cond cond;
    private boolean hasLatterForStmt;
    private ForStmt anoForStmt;
    private Stmt stmt;

    public StmtFor(boolean hasFormerForStmt, ForStmt forStmt, boolean hasCond, Cond cond,
                   boolean hasLatterForStmt, ForStmt anoForStmt, Stmt stmt) {
        this.hasFormerForStmt = hasFormerForStmt;
        this.forStmt = forStmt;
        this.hasCond = hasCond;
        this.cond = cond;
        this.hasLatterForStmt = hasLatterForStmt;
        this.anoForStmt = anoForStmt;
        this.stmt = stmt;
    }

    public void print(StringBuilder strb) {
        strb.append("FORTK for\n");
        strb.append("LPARENT (\n");
        if (hasFormerForStmt) {
            forStmt.print(strb);
        }
        strb.append("SEMICN ;\n");
        if (hasCond) {
            cond.print(strb);
        }
        strb.append("SEMICN ;\n");
        if (hasLatterForStmt) {
            anoForStmt.print(strb);
        }
        strb.append("RPARENT )\n");
        stmt.print(strb);
        strb.append("<Stmt>\n");
    }
}
