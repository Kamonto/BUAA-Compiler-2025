package parser.type;

public class FuncType {
    private boolean isVoid;

    public FuncType(boolean isVoid) {
        this.isVoid = isVoid;
    }

    public void print(StringBuilder strb) {
        if (isVoid) {
            strb.append("VOIDTK void\n");
        }
        else {
            strb.append("INTTK int\n");
        }
        strb.append("<FuncType>\n");
    }
}
