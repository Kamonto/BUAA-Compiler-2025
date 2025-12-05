package symbolizer;

import error.Error;
import error.ErrorList;
import lexer.Token;
import parser.block.BlockItem;
import parser.block.StmtReturn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolTable {
    private Scope scope;
    private HashMap<Integer, ArrayList<Symbol>> symbols;
    private HashMap<Integer, ArrayList<FuncSymbol>> funcSymbols;
    private HashMap<Integer, ArrayList<ArrSymbol>> arrSymbols;
    private HashMap<Integer, ArrayList<ConstSymbol>> constSymbols;
    private ErrorList errorList;

    public SymbolTable(Scope scope, ErrorList errorList) {
        this.scope = scope;
        symbols = new HashMap<>();
        funcSymbols = new HashMap<>();
        arrSymbols = new HashMap<>();
        constSymbols = new HashMap<>();
        this.errorList = errorList;
    }

    public void addSymbol(Symbol symbol) {
        int scope = symbol.getScope();
        if (!symbols.containsKey(scope)) {
            symbols.put(scope, new ArrayList<Symbol>());
        }
        symbols.get(scope).add(symbol);
    }

    public void addFuncSymbol(FuncSymbol funcSymbol) {
        int scope = funcSymbol.getScope();
        if (!funcSymbols.containsKey(scope)) {
            funcSymbols.put(scope, new ArrayList<FuncSymbol>());
        }
        funcSymbols.get(scope).add(funcSymbol);
    }

    public void addArrSymbol(ArrSymbol arrSymbol) {
        int scope = arrSymbol.getScope();
        if (!arrSymbols.containsKey(scope)) {
            arrSymbols.put(scope, new ArrayList<ArrSymbol>());
        }
        arrSymbols.get(scope).add(arrSymbol);
    }

    public void addConstSymbol(ConstSymbol constSymbol) {
        int scope = constSymbol.getScope();
        if (!constSymbols.containsKey(scope)) {
            constSymbols.put(scope, new ArrayList<ConstSymbol>());
        }
        constSymbols.get(scope).add(constSymbol);
    }

    public Symbol getSymbol(int scope, String name) {
        if (!symbols.containsKey(scope)) {
            return null;
        }
        for (Symbol symbol : symbols.get(scope)) {
            if (symbol.getName().equals(name)) {
                return symbol;
            }
        }
        return null;
    }

    public Symbol findSymbol(int scope, String name) {
        if (!symbols.containsKey(scope)) {
            return null;
        }
        for (Symbol symbol : symbols.get(scope)) {
            if (symbol.getName().equals(name) && symbol.getLabel() != null) {
                return symbol;
            }
        }
        return null;
    }

    public FuncSymbol getFuncSymbol(int scope, String name) {
        if (!funcSymbols.containsKey(scope)) {
            return null;
        }
        for (FuncSymbol funcSymbol : funcSymbols.get(scope)) {
            if (funcSymbol.getName().equals(name)) {
                return funcSymbol;
            }
        }
        return null;
    }

    public FuncSymbol findFuncSymbol(int scope, String name) {
        if (!funcSymbols.containsKey(scope)) {
            return null;
        }
        for (FuncSymbol funcSymbol : funcSymbols.get(scope)) {
            if (funcSymbol.getName().equals(name) && funcSymbol.getLabel() != null) {
                return funcSymbol;
            }
        }
        return null;
    }

    public ArrSymbol getArrSymbol(int scope, String name) {
        if (!arrSymbols.containsKey(scope)) {
            return null;
        }
        for (ArrSymbol arrSymbol : arrSymbols.get(scope)) {
            if (arrSymbol.getName().equals(name)) {
                return arrSymbol;
            }
        }
        return null;
    }

    public ArrSymbol findArrSymbol(int scope, String name) {
        if (!arrSymbols.containsKey(scope)) {
            return null;
        }
        for (ArrSymbol arrSymbol : arrSymbols.get(scope)) {
            if (arrSymbol.getName().equals(name) && arrSymbol.getLabel() != null) {
                return arrSymbol;
            }
        }
        return null;
    }

    public ConstSymbol getConstSymbol(int scope, String name) {
        if (!constSymbols.containsKey(scope)) {
            return null;
        }
        for (ConstSymbol constSymbol : constSymbols.get(scope)) {
            if (constSymbol.getName().equals(name)) {
                return constSymbol;
            }
        }
        return null;
    }

    public ConstSymbol findConstSymbol(int scope, String name) {
        if (!constSymbols.containsKey(scope)) {
            return null;
        }
        for (ConstSymbol constSymbol : constSymbols.get(scope)) {
            if (constSymbol.getName().equals(name) && constSymbol.getLabel() != null) {
                return constSymbol;
            }
        }
        return null;
    }

    public void checkDuplicateDeclaration(Token ident) {
        int scopenum = scope.getScope();
        String identname = ident.getContent();
        if (identname.equals("getint")) {
            Error error = new Error(ident.getLine(), 'b');
            errorList.addError(error);
        }
        if (!symbols.containsKey(scopenum)) {
            return;
        }
        for (Symbol symbol : symbols.get(scopenum)) {
            if (symbol.getName().equals(identname)) {
                Error error = new Error(ident.getLine(), 'b');
                errorList.addError(error);
            }
        }
    }

    public void checkUndeclaredVariable(Token ident) {
        ArrayList<Integer> scopeStack = scope.getScopeStack();
        String identname = ident.getContent();
        if (identname.equals("getint")) {
            return;
        }
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            int scopenum = scopeStack.get(i);
            if (!symbols.containsKey(scopenum)) {
                continue;
            }
            for (Symbol symbol : symbols.get(scopenum)) {
                if (symbol.getName().equals(identname)) {
                    return;
                }
            }
        }
        Error error = new Error(ident.getLine(), 'c');
        errorList.addError(error);
    }

    public void checkFuncParamsMismatch(Token ident, FuncSymbol funcSymbol) {
        ArrayList<Integer> scopeStack = scope.getScopeStack();
        String funcname = funcSymbol.getName();
        ArrayList<Boolean> params = funcSymbol.getParams();
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            int scopenum = scopeStack.get(i);
            if (!funcSymbols.containsKey(scopenum)) {
                continue;
            }
            for (FuncSymbol anoFuncSymbol : funcSymbols.get(scopenum)) {
                if (anoFuncSymbol.getName().equals(funcname)) {
                    ArrayList<Boolean> anoParams = anoFuncSymbol.getParams();
                    if (params.size() != anoParams.size()) {
                        Error error = new Error(ident.getLine(), 'd');
                        errorList.addError(error);
                        return;
                    }
                    int size = params.size();
                    for (int j = 0; j < size; j++) {
                        if (params.get(j) != anoParams.get(j)) {
                            Error error = new Error(ident.getLine(), 'e');
                            errorList.addError(error);
                            return;
                        }
                    }
                    return;
                }
            }
        }
    }

    public void checkReturnInVoidFunc(boolean hasReturnValue, Token token) {
        if (scope.getLayer() > 1) {
            FuncSymbol funcSymbol = funcSymbols.get(1).get(funcSymbols.get(1).size() - 1);
            if (funcSymbol.hasReturnValue()) {
                return;
            }
            else if (!funcSymbol.hasReturnValue() && !hasReturnValue) {
                return;
            }
        }
        Error error = new Error(token.getLine(), 'f');
        errorList.addError(error);
    }

    public void checkMissReturnInNonVoidFunc(ArrayList<BlockItem> blockItems, Token token) {
        if (!blockItems.isEmpty()) {
            BlockItem blockItem = blockItems.get(blockItems.size() - 1);
            if (blockItem instanceof StmtReturn) {
                return;
            }
        }
        Error error = new Error(token.getLine(), 'g');
        errorList.addError(error);
    }

    public void checkAssignToConst(Token ident) {
        ArrayList<Integer> scopeStack = scope.getScopeStack();
        String identname = ident.getContent();
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            int scopenum = scopeStack.get(i);
            if (!symbols.containsKey(scopenum)) {
                continue;
            }
            for (Symbol symbol : symbols.get(scopenum)) {
                if (symbol.getName().equals(identname)) {
                    if (symbol.isConstVar()) {
                        Error error = new Error(ident.getLine(), 'h');
                        errorList.addError(error);
                    }
                    return;
                }
            }
        }
    }

    public void checkFormatCharInPrintfMismatch(String stringConst, int expsize, Token token) {
        int count = 0;
        Pattern pattern = Pattern.compile("%d");
        Matcher matcher = pattern.matcher(stringConst);
        while (matcher.find()) {
            count++;
        }
        if (count != expsize) {
            Error error = new Error(token.getLine(), 'l');
            errorList.addError(error);
        }
    }

    public void checkContinueOrBreakOutOfLoop(Token token) {
        if (scope.getLoopLayer() <= 0) {
            Error error = new Error(token.getLine(), 'm');
            errorList.addError(error);
        }
    }

    public StringBuilder print() {
        StringBuilder strb = new StringBuilder();
        int nowMaxScope = scope.getNowMaxScope();
        for (int i = 1; i <= nowMaxScope; i++) {
            if (symbols.containsKey(i)) {
                for (Symbol symbol : symbols.get(i)) {
                    strb.append(symbol.getScope()).append(" ").append(symbol.getName()).append(" ").append(symbol.getType().toString()).append("\n");
                }
            }
        }
        return strb;
    }

    public boolean isArray(Scope scope, Token ident) {
        ArrayList<Integer> scopeStack = scope.getScopeStack();
        String identname = ident.getContent();
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            int scopenum = scopeStack.get(i);
            if (!symbols.containsKey(scopenum)) {
                continue;
            }
            for (Symbol symbol : symbols.get(scopenum)) {
                if (symbol.getName().equals(identname)) {
                    return symbol.isArray();
                }
            }
        }
        return false;
    }
}
