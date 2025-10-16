package parser.function;

import java.util.ArrayList;

public class FuncFParams {
    private ArrayList<FuncFParam> funcFParams;

    public FuncFParams(ArrayList<FuncFParam> funcFParams) {
        this.funcFParams = funcFParams;
    }

    public void print(StringBuilder strb) {
        int size = funcFParams.size();
        if (size > 0) {
            funcFParams.get(0).print(strb);
            for (int i = 1; i < size; i++) {
                strb.append("COMMA ,\n");
                funcFParams.get(i).print(strb);
            }
        }
        strb.append("<FuncFParams>\n");
    }
}
