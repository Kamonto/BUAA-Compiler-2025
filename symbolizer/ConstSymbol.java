package symbolizer;

import java.util.ArrayList;

public class ConstSymbol {
    private int scope;
    private int layer;
    private String name;
    private int value;
    private ArrayList<Integer> values;
    private String label;

    public ConstSymbol(Scope scope, String name, int value) {
        this.scope = scope.getScope();
        this.layer = scope.getLayer();
        this.name = name;
        this.value = value;
        this.values = null;
        this.label = null;
    }

    public ConstSymbol(Scope scope, String name, ArrayList<Integer> values) {
        this.scope = scope.getScope();
        this.layer = scope.getLayer();
        this.name = name;
        this.value = -1;
        this.values = values;
        this.label = null;
    }

    public int getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getValue(int index) {
        return values.get(index);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
