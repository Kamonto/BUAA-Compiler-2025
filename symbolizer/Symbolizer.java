package symbolizer;

import error.ErrorList;
import parser.CompUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Symbolizer {
    private final CompUnit compUnit;
    private Scope scope;
    private SymbolTable symbols;
    private ErrorList errorList;

    public Symbolizer(CompUnit compUnit, ErrorList errorList) {
        this.compUnit = compUnit;
        scope = new Scope();
        this.errorList = errorList;
        symbols = new SymbolTable(scope, errorList);
    }

    public SymbolTable symbolize() {
        compUnit.symbolize(symbols, scope);
        return symbols;
    }

    public void printSymbols() throws IOException {
        Path path = Paths.get("symbol.txt");
        Files.write(path, symbols.print().toString().getBytes());
    }
}
