package symbolizer;

public class ArrSymbol {
    private int scope;
    private int layer;
    private String name;
    private int size;
    private String label;

    public ArrSymbol(Scope scope, String name, int size) {
        this.scope = scope.getScope();
        this.layer = scope.getLayer();
        this.name = name;
        this.size = size;
        this.label = null;
    }

    public int getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
