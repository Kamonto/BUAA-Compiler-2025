package symbolizer;

public class Symbol{
    private int scope;
    private int layer;
    private String name;
    private SymbolType type;
    private String label;

    public Symbol(Scope scope, String name, SymbolType type) {
        this.scope = scope.getScope();
        this.layer = scope.getLayer();
        this.name = name;
        this.type = type;
        this.label = null;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }

    public SymbolType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public boolean isConstVar() {
        return (type == SymbolType.ConstInt) || (type == SymbolType.ConstIntArray);
    }

    public boolean isArray() {
        return (type == SymbolType.ConstIntArray) || (type == SymbolType.StaticIntArray) || (type == SymbolType.IntArray);
    }
}
