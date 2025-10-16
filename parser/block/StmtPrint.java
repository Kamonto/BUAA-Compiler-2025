package parser.block;

import parser.expression.Exp;

import java.util.ArrayList;

public class StmtPrint implements Stmt {
    private String stringConst;
    private ArrayList<Exp> exps;

    public StmtPrint(String stringConst, ArrayList<Exp> exps) {
        this.stringConst = stringConst;
        this.exps = exps;
    }

    public void print(StringBuilder strb) {
        strb.append("PRINTFTK printf\n");
        strb.append("LPARENT (\n");
        strb.append("STRCON ").append(stringConst).append("\n");
        for (Exp exp : exps) {
            strb.append("COMMA ,\n");
            exp.print(strb);
        }
        strb.append("RPARENT )\n");
        strb.append("SEMICN ;\n");
        strb.append("<Stmt>\n");
    }
}
