package parser.block;

public class StmtBreak implements Stmt {
    public void print(StringBuilder strb) {
        strb.append("BREAKTK break\n");
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }
}
