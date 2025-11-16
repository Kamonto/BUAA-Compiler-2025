package parser;

import error.Error;
import error.ErrorList;
import lexer.Token;
import lexer.TokenType;
import parser.block.*;
import parser.declaration.*;
import parser.expression.*;
import parser.function.*;
import parser.type.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static lexer.TokenType.*;

public class Parser {
    private final ArrayList<Token> tokens;
    private final int size;
    private int nowat;
    private CompUnit compUnit;
    private ErrorList errorList;

    public Parser(ArrayList<Token> tokens, ErrorList errorList) {
        this.tokens = tokens;
        size = tokens.size();
        nowat = 0;
        compUnit = null;
        this.errorList = errorList;
    }

    public CompUnit parse() {
        compUnit = parseCompUnit();
        return compUnit;
    }

    private CompUnit parseCompUnit() {
        ArrayList<Decl> decls = new ArrayList<Decl>();
        ArrayList<FuncDef> funcDefs = new ArrayList<FuncDef>();
        MainFuncDef mainFuncDef;
        TokenType token0 = getTokenFromTokens(nowat).getTokenType();
        TokenType token1 = getTokenFromTokens(nowat + 1).getTokenType();
        TokenType token2 = getTokenFromTokens(nowat + 2).getTokenType();
        while (token0 == CONSTTK || token0 == STATICTK || (token0 == INTTK && token1 == IDENFR &&
                (token2 == ASSIGN || token2 == LBRACK || token2 == COMMA || token2 == SEMICN))) {
            decls.add(parseDecl());
            token0 = getTokenFromTokens(nowat).getTokenType();
            token1 = getTokenFromTokens(nowat + 1).getTokenType();
            token2 = getTokenFromTokens(nowat + 2).getTokenType();
        }
        while (token0 == VOIDTK || (token0 == INTTK && token1 == IDENFR && token2 == LPARENT)) {
            funcDefs.add(parseFuncDef());
            token0 = getTokenFromTokens(nowat).getTokenType();
            token1 = getTokenFromTokens(nowat + 1).getTokenType();
            token2 = getTokenFromTokens(nowat + 2).getTokenType();
        }
        mainFuncDef = parseMainFuncDef();
        return new CompUnit(decls, funcDefs, mainFuncDef);
    }

    private Decl parseDecl() {
        if (getTokenFromTokens(nowat).getTokenType() == CONSTTK) {
            return parseConstDecl();
        }
        else {
            return parseVarDecl();
        }
    }

    private ConstDecl parseConstDecl() {
        BType bType;
        ArrayList<ConstDef> constDefs = new ArrayList<ConstDef>();
        if (getTokenFromTokens(nowat).getTokenType() != CONSTTK) {
            error(getTokenFromTokens(nowat - 1).getLine(), CONSTTK, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        bType = parseBType();
        constDefs.add(parseConstDef());
        while (getTokenFromTokens(nowat).getTokenType() == COMMA) {
            nowat++;
            constDefs.add(parseConstDef());
        }
        if (getTokenFromTokens(nowat).getTokenType() != SEMICN) {
            error(getTokenFromTokens(nowat - 1).getLine(), SEMICN, getTokenFromTokens(nowat).getTokenType());
            errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'i'));
        }
        else {
            nowat++;
        }
        return new ConstDecl(bType, constDefs);
    }

    private ConstDef parseConstDef() {
        Token ident;
        boolean isArray;
        ConstExp constExp;
        ConstInitVal constInitVal;
        if (getTokenFromTokens(nowat).getTokenType() != IDENFR) {
            error(getTokenFromTokens(nowat - 1).getLine(), IDENFR, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        ident = getTokenFromTokens(nowat);
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() == LBRACK) {
            nowat++;
            isArray = true;
            constExp = parseConstExp();
            if (getTokenFromTokens(nowat).getTokenType() != RBRACK) {
                error(getTokenFromTokens(nowat - 1).getLine(), RBRACK, getTokenFromTokens(nowat).getTokenType());
                errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'k'));
            }
            else {
                nowat++;
            }
        }
        else {
            isArray = false;
            constExp = null;
        }
        if (getTokenFromTokens(nowat).getTokenType() != ASSIGN) {
            error(getTokenFromTokens(nowat - 1).getLine(), ASSIGN, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        constInitVal = parseConstInitVal();
        return new ConstDef(ident, isArray, constExp, constInitVal);
    }

    private ConstInitVal parseConstInitVal() {
        boolean isArray;
        ConstExp constExp;
        ArrayList<ConstExp> constExps = new ArrayList<ConstExp>();
        if (getTokenFromTokens(nowat).getTokenType() == LBRACE) {
            nowat++;
            isArray = true;
            constExp = null;
            while (getTokenFromTokens(nowat).getTokenType() != RBRACE) {
                constExps.add(parseConstExp());
                if (getTokenFromTokens(nowat).getTokenType() == COMMA) {
                    nowat++;
                }
            }
            nowat++;
        }
        else {
            isArray = false;
            constExp = parseConstExp();
        }
        return new ConstInitVal(isArray, constExp, constExps);
    }

    private VarDecl parseVarDecl() {
        boolean isStatic;
        BType bType;
        ArrayList<VarDef> varDefs = new ArrayList<VarDef>();
        if (getTokenFromTokens(nowat).getTokenType() == STATICTK) {
            nowat++;
            isStatic = true;
        }
        else {
            isStatic = false;
        }
        bType = parseBType();
        varDefs.add(parseVarDef());
        while (getTokenFromTokens(nowat).getTokenType() == COMMA) {
            nowat++;
            varDefs.add(parseVarDef());
        }
        if (getTokenFromTokens(nowat).getTokenType() != SEMICN) {
            error(getTokenFromTokens(nowat - 1).getLine(), SEMICN, getTokenFromTokens(nowat).getTokenType());
            errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'i'));
        }
        else {
            nowat++;
        }
        return new VarDecl(isStatic, bType, varDefs);
    }

    private VarDef parseVarDef() {
        Token ident;
        boolean isArray;
        ConstExp constExp;
        boolean hasInitValue;
        InitVal initVal;
        if (getTokenFromTokens(nowat).getTokenType() != IDENFR) {
            error(getTokenFromTokens(nowat - 1).getLine(), IDENFR, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        ident = getTokenFromTokens(nowat);
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() == LBRACK) {
            nowat++;
            isArray = true;
            constExp = parseConstExp();
            if (getTokenFromTokens(nowat).getTokenType() != RBRACK) {
                error(getTokenFromTokens(nowat - 1).getLine(), RBRACK, getTokenFromTokens(nowat).getTokenType());
                errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'k'));
            }
            else {
                nowat++;
            }
        }
        else {
            isArray = false;
            constExp = null;
        }
        if (getTokenFromTokens(nowat).getTokenType() == ASSIGN) {
            nowat++;
            hasInitValue = true;
            initVal = parseInitVal();
        }
        else {
            hasInitValue = false;
            initVal = null;
        }
        return new VarDef(ident, isArray, constExp, hasInitValue, initVal);
    }

    private InitVal parseInitVal() {
        boolean isArray;
        Exp exp;
        ArrayList<Exp> exps = new ArrayList<Exp>();
        if (getTokenFromTokens(nowat).getTokenType() == LBRACE) {
            nowat++;
            isArray = true;
            exp = null;
            while (getTokenFromTokens(nowat).getTokenType() != RBRACE) {
                exps.add(parseExp());
                if (getTokenFromTokens(nowat).getTokenType() == COMMA) {
                    nowat++;
                }
            }
            nowat++;
        }
        else {
            isArray = false;
            exp = parseExp();
        }
        return new InitVal(isArray, exp, exps);
    }

    private FuncDef parseFuncDef() {
        FuncType funcType;
        Token ident;
        boolean hasFuncFParams;
        FuncFParams funcParams;
        Block block;
        funcType = parseFuncType();
        if (getTokenFromTokens(nowat).getTokenType() != IDENFR) {
            error(getTokenFromTokens(nowat - 1).getLine(), IDENFR, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        ident = getTokenFromTokens(nowat);
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() != LPARENT) {
            error(getTokenFromTokens(nowat - 1).getLine(), LPARENT, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() == INTTK) {
            hasFuncFParams = true;
            funcParams = parseFuncFParams();
        }
        else {
            hasFuncFParams = false;
            funcParams = null;
        }
        if (getTokenFromTokens(nowat).getTokenType() != RPARENT) {
            error(getTokenFromTokens(nowat - 1).getLine(), RPARENT, getTokenFromTokens(nowat).getTokenType());
            errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'j'));
        }
        else {
            nowat++;
        }
        block = parseBlock();
        return new FuncDef(funcType, ident, hasFuncFParams, funcParams, block);
    }

    private FuncFParams parseFuncFParams() {
        ArrayList<FuncFParam> funcParams = new ArrayList<FuncFParam>();
        funcParams.add(parseFuncFParam());
        while (getTokenFromTokens(nowat).getTokenType() == COMMA) {
            nowat++;
            funcParams.add(parseFuncFParam());
        }
        return new FuncFParams(funcParams);
    }

    private FuncFParam parseFuncFParam() {
        BType bType;
        Token ident;
        boolean isArray;
        bType = parseBType();
        if (getTokenFromTokens(nowat).getTokenType() != IDENFR) {
            error(getTokenFromTokens(nowat - 1).getLine(), IDENFR, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        ident = getTokenFromTokens(nowat);
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() == LBRACK) {
            isArray = true;
            nowat++;
            if (getTokenFromTokens(nowat).getTokenType() != RBRACK) {
                error(getTokenFromTokens(nowat - 1).getLine(), RBRACK, getTokenFromTokens(nowat).getTokenType());
                errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'k'));
            }
            else {
                nowat++;
            }
        }
        else {
            isArray = false;
        }
        return new FuncFParam(bType, ident, isArray);
    }

    private MainFuncDef parseMainFuncDef() {
        Block block;
        if (getTokenFromTokens(nowat).getTokenType() != INTTK) {
            error(getTokenFromTokens(nowat - 1).getLine(), INTTK, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() != MAINTK) {
            error(getTokenFromTokens(nowat - 1).getLine(), MAINTK, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() != LPARENT) {
            error(getTokenFromTokens(nowat - 1).getLine(), LPARENT, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() != RPARENT) {
            error(getTokenFromTokens(nowat - 1).getLine(), RPARENT, getTokenFromTokens(nowat).getTokenType());
            errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'j'));
        }
        else {
            nowat++;
        }
        block = parseBlock();
        return new MainFuncDef(block);
    }

    private Block parseBlock() {
        Token rBraceToken;
        ArrayList<BlockItem> blockItems = new ArrayList<BlockItem>();
        if (getTokenFromTokens(nowat).getTokenType() != LBRACE) {
            error(getTokenFromTokens(nowat - 1).getLine(), LBRACE, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        while (getTokenFromTokens(nowat).getTokenType() != RBRACE) {
            blockItems.add(parseBlockItem());
        }
        rBraceToken = getTokenFromTokens(nowat);
        nowat++;
        return new Block(rBraceToken, blockItems);
    }

    private BlockItem parseBlockItem() {
        TokenType token0 = getTokenFromTokens(nowat).getTokenType();
        if (token0 == CONSTTK || token0 == STATICTK || token0 == INTTK) {
            return parseDecl();
        }
        else {
            return parseStmt();
        }
    }

    private Stmt parseStmt() {
        TokenType token0 = getTokenFromTokens(nowat).getTokenType();
        if (token0 == LBRACE) {
            return parseStmtBlock();
        }
        else if (token0 == IFTK) {
            return parseStmtIf();
        }
        else if (token0 == FORTK) {
            return parseStmtFor();
        }
        else if (token0 == BREAKTK) {
            return parseStmtBreak();
        }
        else if (token0 == CONTINUETK) {
            return parseStmtContinue();
        }
        else if (token0 == RETURNTK) {
            return parseStmtReturn();
        }
        else if (token0 == PRINTFTK) {
            return parseStmtPrint();
        }
        else {
            for (int i = nowat; i < size; i++) {
                if (getTokenFromTokens(i).getTokenType() == ASSIGN) {
                    return parseStmtAssign();
                }
                if (getTokenFromTokens(i).getTokenType() == SEMICN) {
                    return parseStmtExp();
                }
            }
            return parseStmtExp();
        }
    }

    private StmtAssign parseStmtAssign() {
        LVal lVal;
        Exp exp;
        lVal = parseLVal();
        if (getTokenFromTokens(nowat).getTokenType() != ASSIGN) {
            error(getTokenFromTokens(nowat - 1).getLine(), ASSIGN, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        exp = parseExp();
        if (getTokenFromTokens(nowat).getTokenType() != SEMICN) {
            error(getTokenFromTokens(nowat - 1).getLine(), SEMICN, getTokenFromTokens(nowat).getTokenType());
            errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'i'));
        }
        else {
            nowat++;
        }
        return new StmtAssign(lVal, exp);
    }

    private StmtExp parseStmtExp() {
        boolean hasExp;
        Exp exp;
        if (getTokenFromTokens(nowat).getTokenType() == SEMICN) {
            hasExp = false;
            exp = null;
            nowat++;
        }
        else {
            hasExp = true;
            exp = parseExp();
            if (getTokenFromTokens(nowat).getTokenType() != SEMICN) {
                error(getTokenFromTokens(nowat - 1).getLine(), SEMICN, getTokenFromTokens(nowat).getTokenType());
                errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'i'));
            }
            else {
                nowat++;
            }
        }
        return new StmtExp(hasExp, exp);
    }

    private StmtBlock parseStmtBlock() {
        Block block = parseBlock();
        return new StmtBlock(block);
    }

    private StmtIf parseStmtIf() {
        Cond cond;
        Stmt stmt;
        boolean hasElse;
        Stmt anostmt;
        if (getTokenFromTokens(nowat).getTokenType() != IFTK) {
            error(getTokenFromTokens(nowat - 1).getLine(), IFTK, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() != LPARENT) {
            error(getTokenFromTokens(nowat - 1).getLine(), LPARENT, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        cond = parseCond();
        if (getTokenFromTokens(nowat).getTokenType() != RPARENT) {
            error(getTokenFromTokens(nowat - 1).getLine(), RPARENT, getTokenFromTokens(nowat).getTokenType());
            errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'j'));
        }
        else {
            nowat++;
        }
        stmt = parseStmt();
        if (getTokenFromTokens(nowat).getTokenType() == ELSETK) {
            nowat++;
            hasElse = true;
            anostmt = parseStmt();
        }
        else {
            hasElse = false;
            anostmt = null;
        }
        return new StmtIf(cond, stmt, hasElse, anostmt);
    }

    private StmtFor parseStmtFor() {
        boolean hasFormerForStmt;
        ForStmt forStmt;
        boolean hasCond;
        Cond cond;
        boolean hasLatterForStmt;
        ForStmt anoForStmt;
        Stmt stmt;
        if (getTokenFromTokens(nowat).getTokenType() != FORTK) {
            error(getTokenFromTokens(nowat - 1).getLine(), FORTK, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() != LPARENT) {
            error(getTokenFromTokens(nowat - 1).getLine(), LPARENT, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() == SEMICN) {
            hasFormerForStmt = false;
            forStmt = null;
        }
        else {
            hasFormerForStmt = true;
            forStmt = parseForStmt();
            if (getTokenFromTokens(nowat).getTokenType() != SEMICN) {
                error(getTokenFromTokens(nowat - 1).getLine(), SEMICN, getTokenFromTokens(nowat).getTokenType());
                return null;
            }
        }
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() == SEMICN) {
            hasCond = false;
            cond = null;
        }
        else {
            hasCond = true;
            cond = parseCond();
            if (getTokenFromTokens(nowat).getTokenType() != SEMICN) {
                error(getTokenFromTokens(nowat - 1).getLine(), SEMICN, getTokenFromTokens(nowat).getTokenType());
                return null;
            }
        }
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() == RPARENT) {
            hasLatterForStmt = false;
            anoForStmt = null;
        }
        else {
            hasLatterForStmt = true;
            anoForStmt = parseForStmt();
            if (getTokenFromTokens(nowat).getTokenType() != RPARENT) {
                error(getTokenFromTokens(nowat - 1).getLine(), RPARENT, getTokenFromTokens(nowat).getTokenType());
                return null;
            }
        }
        nowat++;
        stmt = parseStmt();
        return new StmtFor(hasFormerForStmt, forStmt, hasCond, cond, hasLatterForStmt, anoForStmt, stmt);
    }

    private StmtBreak parseStmtBreak() {
        Token breakToken;
        if (getTokenFromTokens(nowat).getTokenType() != BREAKTK) {
            error(getTokenFromTokens(nowat - 1).getLine(), BREAKTK, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        breakToken = getTokenFromTokens(nowat);
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() != SEMICN) {
            error(getTokenFromTokens(nowat - 1).getLine(), SEMICN, getTokenFromTokens(nowat).getTokenType());
            errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'i'));
        }
        else {
            nowat++;
        }
        return new StmtBreak(breakToken);
    }

    private StmtContinue parseStmtContinue() {
        Token continueToken;
        if (getTokenFromTokens(nowat).getTokenType() != CONTINUETK) {
            error(getTokenFromTokens(nowat - 1).getLine(), CONTINUETK, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        continueToken = getTokenFromTokens(nowat);
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() != SEMICN) {
            error(getTokenFromTokens(nowat - 1).getLine(), SEMICN, getTokenFromTokens(nowat).getTokenType());
            errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'i'));
        }
        else {
            nowat++;
        }
        return new StmtContinue(continueToken);
    }

    private StmtReturn parseStmtReturn() {
        Token returnToken;
        boolean hasReturnValue;
        Exp exp;
        if (getTokenFromTokens(nowat).getTokenType() != RETURNTK) {
            error(getTokenFromTokens(nowat - 1).getLine(), RETURNTK, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        returnToken = getTokenFromTokens(nowat);
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() == SEMICN) {
            hasReturnValue = false;
            exp = null;
            nowat++;
        }
        else {
            hasReturnValue = true;
            exp = parseExp();
            if (getTokenFromTokens(nowat).getTokenType() != SEMICN) {
                error(getTokenFromTokens(nowat - 1).getLine(), SEMICN, getTokenFromTokens(nowat).getTokenType());
                errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'i'));
            }
            else {
                nowat++;
            }
        }
        return new StmtReturn(returnToken, hasReturnValue, exp);
    }

    private StmtPrint parseStmtPrint() {
        Token printfToken;
        String stringConst;
        ArrayList<Exp> exps = new ArrayList<Exp>();
        if (getTokenFromTokens(nowat).getTokenType() != PRINTFTK) {
            error(getTokenFromTokens(nowat - 1).getLine(), PRINTFTK, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        printfToken = getTokenFromTokens(nowat);
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() != LPARENT) {
            error(getTokenFromTokens(nowat - 1).getLine(), LPARENT, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() != STRCON) {
            error(getTokenFromTokens(nowat - 1).getLine(), STRCON, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        stringConst = getTokenFromTokens(nowat).getContent();
        nowat++;
        while (getTokenFromTokens(nowat).getTokenType() == COMMA) {
            nowat++;
            exps.add(parseExp());
        }
        if (getTokenFromTokens(nowat).getTokenType() != RPARENT) {
            error(getTokenFromTokens(nowat - 1).getLine(), RPARENT, getTokenFromTokens(nowat).getTokenType());
            errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'j'));
        }
        else {
            nowat++;
        }
        if (getTokenFromTokens(nowat).getTokenType() != SEMICN) {
            error(getTokenFromTokens(nowat - 1).getLine(), SEMICN, getTokenFromTokens(nowat).getTokenType());
            errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'i'));
        }
        else {
            nowat++;
        }
        return new StmtPrint(printfToken, stringConst, exps);
    }

    private Cond parseCond() {
        LOrExp lOrExp = parseLOrExp();
        return new Cond(lOrExp);
    }

    private ForStmt parseForStmt() {
        ArrayList<LVal> lVals = new ArrayList<LVal>();
        ArrayList<Exp> exps = new ArrayList<Exp>();
        lVals.add(parseLVal());
        if (getTokenFromTokens(nowat).getTokenType() != ASSIGN) {
            error(getTokenFromTokens(nowat - 1).getLine(), ASSIGN, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        exps.add(parseExp());
        while (getTokenFromTokens(nowat).getTokenType() == COMMA) {
            nowat++;
            lVals.add(parseLVal());
            if (getTokenFromTokens(nowat).getTokenType() != ASSIGN) {
                error(getTokenFromTokens(nowat - 1).getLine(), ASSIGN, getTokenFromTokens(nowat).getTokenType());
                return null;
            }
            nowat++;
            exps.add(parseExp());
        }
        return new ForStmt(lVals, exps);
    }

    private LOrExp parseLOrExp() {
        ArrayList<LAndExp> lAndExps = new ArrayList<LAndExp>();
        lAndExps.add(parseLAndExp());
        while (getTokenFromTokens(nowat).getTokenType() == OR) {
            nowat++;
            lAndExps.add(parseLAndExp());
        }
        return new LOrExp(lAndExps);
    }

    private LAndExp parseLAndExp() {
        ArrayList<EqExp> eqExps = new ArrayList<EqExp>();
        eqExps.add(parseEqExp());
        while (getTokenFromTokens(nowat).getTokenType() == AND) {
            nowat++;
            eqExps.add(parseEqExp());
        }
        return new LAndExp(eqExps);
    }

    private EqExp parseEqExp() {
        ArrayList<RelExp> relExps = new ArrayList<RelExp>();
        ArrayList<Integer> opTypes = new ArrayList<Integer>();
        relExps.add(parseRelExp());
        opTypes.add(0);
        TokenType token0 = getTokenFromTokens(nowat).getTokenType();
        while (token0 == EQL || token0 == NEQ) {
            nowat++;
            if (token0 == EQL) {
                relExps.add(parseRelExp());
                opTypes.add(1);
            }
            else {
                relExps.add(parseRelExp());
                opTypes.add(2);
            }
            token0 = getTokenFromTokens(nowat).getTokenType();
        }
        return new EqExp(relExps, opTypes);
    }

    private RelExp parseRelExp() {
        ArrayList<AddExp> addExps = new ArrayList<AddExp>();
        ArrayList<Integer> opTypes = new ArrayList<Integer>();
        addExps.add(parseAddExp());
        opTypes.add(0);
        TokenType token0 = getTokenFromTokens(nowat).getTokenType();
        while (token0 == LSS || token0 == GRE || token0 == LEQ || token0 == GEQ) {
            nowat++;
            if (token0 == LSS) {
                addExps.add(parseAddExp());
                opTypes.add(1);
            }
            else if (token0 == GRE) {
                addExps.add(parseAddExp());
                opTypes.add(2);
            }
            else if (token0 == LEQ) {
                addExps.add(parseAddExp());
                opTypes.add(3);
            }
            else {
                addExps.add(parseAddExp());
                opTypes.add(4);
            }
            token0 = getTokenFromTokens(nowat).getTokenType();
        }
        return new RelExp(addExps,opTypes);
    }

    private Exp parseExp() {
        AddExp addExp =  parseAddExp();
        return new Exp(addExp);
    }

    private ConstExp parseConstExp() {
        AddExp addExp =  parseAddExp();
        return new ConstExp(addExp);
    }

    private AddExp parseAddExp() {
        ArrayList<MulExp> mulExps = new ArrayList<MulExp>();
        ArrayList<Integer> opTypes = new ArrayList<Integer>();
        mulExps.add(parseMulExp());
        opTypes.add(0);
        TokenType token0 = getTokenFromTokens(nowat).getTokenType();
        while (token0 == PLUS || token0 == MINU) {
            nowat++;
            if (token0 == PLUS) {
                mulExps.add(parseMulExp());
                opTypes.add(1);
            }
            else {
                mulExps.add(parseMulExp());
                opTypes.add(2);
            }
            token0 = getTokenFromTokens(nowat).getTokenType();
        }
        return new AddExp(mulExps, opTypes);
    }

    private MulExp parseMulExp() {
        ArrayList<UnaryExp> unaryExps = new ArrayList<UnaryExp>();
        ArrayList<Integer> opTypes = new ArrayList<Integer>();
        unaryExps.add(parseUnaryExp());
        opTypes.add(0);
        TokenType token0 = getTokenFromTokens(nowat).getTokenType();
        while (token0 == MULT || token0 == DIV || token0 == MOD) {
            nowat++;
            if (token0 == MULT) {
                unaryExps.add(parseUnaryExp());
                opTypes.add(1);
            }
            else if (token0 == DIV) {
                unaryExps.add(parseUnaryExp());
                opTypes.add(2);
            }
            else {
                unaryExps.add(parseUnaryExp());
                opTypes.add(3);
            }
            token0 = getTokenFromTokens(nowat).getTokenType();
        }
        return new MulExp(unaryExps, opTypes);
    }

    private UnaryExp parseUnaryExp() {
        ArrayList<UnaryOp> unaryOps = new ArrayList<UnaryOp>();
        boolean isPrimaryExp;
        PrimaryExp primaryExp;
        Token ident;
        boolean hasFuncRParams;
        FuncRParams funcRParams;
        TokenType token0 = getTokenFromTokens(nowat).getTokenType();
        while (token0 == PLUS || token0 == MINU || token0 == NOT) {
            unaryOps.add(parseUnaryOp());
            token0 = getTokenFromTokens(nowat).getTokenType();
        }
        TokenType token1 = getTokenFromTokens(nowat + 1).getTokenType();
        if (token0 == IDENFR && token1 == LPARENT) {
            isPrimaryExp = false;
            primaryExp = null;
            ident = getTokenFromTokens(nowat);
            nowat++;
            if (getTokenFromTokens(nowat).getTokenType() != LPARENT) {
                error(getTokenFromTokens(nowat - 1).getLine(), LPARENT, getTokenFromTokens(nowat).getTokenType());
                return null;
            }
            nowat++;
            if (getTokenFromTokens(nowat).getTokenType() != RPARENT) {
                hasFuncRParams = true;
                funcRParams = parseFuncRParams();
                if (getTokenFromTokens(nowat).getTokenType() != RPARENT) {
                    error(getTokenFromTokens(nowat - 1).getLine(), RPARENT, getTokenFromTokens(nowat).getTokenType());
                    errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'j'));
                }
                else {
                    nowat++;
                }
            }
            else {
                hasFuncRParams = false;
                funcRParams = null;
                nowat++;
            }
        }
        else {
            isPrimaryExp = true;
            primaryExp = parsePrimaryExp();
            ident = null;
            hasFuncRParams = false;
            funcRParams = null;
        }
        return new UnaryExp(unaryOps, isPrimaryExp, primaryExp, ident, hasFuncRParams, funcRParams);
    }

    private PrimaryExp parsePrimaryExp() {
        int type;
        Exp exp;
        LVal lVal;
        Numbear numbear;
        if (getTokenFromTokens(nowat).getTokenType() == LPARENT) {
            nowat++;
            type = 0;
            exp = parseExp();
            lVal = null;
            numbear = null;
            if (getTokenFromTokens(nowat).getTokenType() != RPARENT) {
                error(getTokenFromTokens(nowat - 1).getLine(), RPARENT, getTokenFromTokens(nowat).getTokenType());
                errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'j'));
            }
            else {
                nowat++;
            }
        }
        else if (getTokenFromTokens(nowat).getTokenType() == IDENFR) {
            type = 1;
            exp = null;
            lVal = parseLVal();
            numbear = null;
        }
        else {
            type = 2;
            exp = null;
            lVal = null;
            numbear = parseNumbear();
        }
        return new PrimaryExp(type, exp, lVal, numbear);
    }

    private FuncRParams parseFuncRParams() {
        ArrayList<Exp> exps = new ArrayList<Exp>();
        exps.add(parseExp());
        while (getTokenFromTokens(nowat).getTokenType() == COMMA) {
            nowat++;
            exps.add(parseExp());
        }
        return new FuncRParams(exps);
    }

    private UnaryOp parseUnaryOp() {
        int type;
        if (getTokenFromTokens(nowat).getTokenType() == PLUS) {
            type = 1;
        }
        else if (getTokenFromTokens(nowat).getTokenType() == MINU) {
            type = -1;
        }
        else if (getTokenFromTokens(nowat).getTokenType() == NOT) {
            type = 0;
        }
        else {
            error(getTokenFromTokens(nowat - 1).getLine(), NOT, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        return new UnaryOp(type);
    }

    private LVal parseLVal() {
        Token ident;
        boolean isArray;
        Exp exp;
        if (getTokenFromTokens(nowat).getTokenType() != IDENFR) {
            error(getTokenFromTokens(nowat - 1).getLine(), IDENFR, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        ident = getTokenFromTokens(nowat);
        nowat++;
        if (getTokenFromTokens(nowat).getTokenType() == LBRACK) {
            nowat++;
            isArray = true;
            exp = parseExp();
            if (getTokenFromTokens(nowat).getTokenType() != RBRACK) {
                error(getTokenFromTokens(nowat - 1).getLine(), RBRACK, getTokenFromTokens(nowat).getTokenType());
                errorList.addError(new Error(getTokenFromTokens(nowat - 1).getLine(), 'k'));
            }
            else {
                nowat++;
            }
        }
        else {
            isArray = false;
            exp = null;
        }
        return new LVal(ident, isArray, exp);
    }

    private Numbear parseNumbear() {
        String intConst;
        if (getTokenFromTokens(nowat).getTokenType() != INTCON) {
            error(getTokenFromTokens(nowat - 1).getLine(), INTCON, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        intConst = getTokenFromTokens(nowat).getContent();
        nowat++;
        return new Numbear(intConst);
    }

    private BType parseBType() {
        if (getTokenFromTokens(nowat).getTokenType() != INTTK) {
            error(getTokenFromTokens(nowat - 1).getLine(), INTTK, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        nowat++;
        return new BType();
    }

    private FuncType parseFuncType() {
        boolean isVoid;
        if (getTokenFromTokens(nowat).getTokenType() == VOIDTK) {
            nowat++;
            isVoid = true;
        }
        else if (getTokenFromTokens(nowat).getTokenType() == INTTK) {
            nowat++;
            isVoid = false;
        }
        else {
            error(getTokenFromTokens(nowat - 1).getLine(), VOIDTK, getTokenFromTokens(nowat).getTokenType());
            return null;
        }
        return new FuncType(isVoid);
    }

    private Token getTokenFromTokens(int index) {
        if (index >= 0 && index < size) {
            return tokens.get(index);
        }
        else {
            return new Token(NONE, "", -1);
        }
    }

    public void printAST() throws IOException {
        StringBuilder strb = new StringBuilder();
        compUnit.print(strb);
        Path path = Paths.get("parser.txt");
        Files.write(path, strb.toString().getBytes());
    }

    private void error(int line, TokenType needTokenType, TokenType gotTokenType) {
        // System.out.println("Syntax Error at Line " + line + ": We need token '" + needTokenType + "', but got token '" + gotTokenType + "'!");
    }
}
