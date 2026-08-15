## 2025 编译实验期中考试

编译实验期中期末考试时间均为两小时，使用机房电脑，IDE 为 IDEA（Java）或 CLion（C++），平台为 judge 平台，允许携带纸质资料。

总体来说时间比较充裕，只要不出事故的话就问题不大。大家课下一定要做好充分测试，防止考试的时候课下强测暴雷；以及考试开始前一定要提前到机房调好 IDE ，确保代码能正常运行和调试。

期中考试非常水，就是加两个文法，只需要根据题目要求依次修改词法分析、语法分析、错误处理三个部分即可。

### 增量开发题

(1) 增加 Token `elif` ，TokenType 为 `ELIFTK` ；
(2) 修改文法 `Stmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]` 为 `Stmt → 'if' '(' Cond ')' Stmt { 'elif' '(' Cond ')' Stmt } [ 'else' Stmt ]` ，可能出现缺右小括号错误；
(3) 增加文法 `FuncStmt → FuncType Ident '(' [FuncFParams] ')' ';'` ，可能出现缺右小括号错误和**缺分号错误**；
(4) 修改文法 `CompUnit → {Decl} {FuncDef} MainFuncDef` 为 `CompUnit → {Decl} {FuncDef | FuncStmt} MainFuncDef` 。