package parser.block;

public class StmtContinue implements Stmt {
    public void print(StringBuilder strb) {
        strb.append("CONTINUETK continue\n");
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }
}
