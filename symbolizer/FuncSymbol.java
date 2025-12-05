package symbolizer;

import java.util.ArrayList;

public class FuncSymbol {
    private int scope;
    private int layer;
    private String name;
    private boolean hasReturnValue;
    private ArrayList<Boolean> params; // true: isArray, false: isNotArray
    private String label;

    public FuncSymbol(Scope scope, String name, boolean hasReturnValue) {
        this.scope = scope.getScope();
        this.layer = scope.getLayer();
        this.name = name;
        this.hasReturnValue = hasReturnValue;
        params = new ArrayList<Boolean>();
        label = null;
    }

    public int getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }

    public boolean hasReturnValue() {
        return hasReturnValue;
    }

    public void addParam(boolean isArray) {
        params.add(isArray);
    }

    public ArrayList<Boolean> getParams() {
        return params;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
