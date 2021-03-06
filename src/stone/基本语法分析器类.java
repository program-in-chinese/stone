package stone;
import static stone.Parser.rule;

import java.util.HashSet;

import stone.Parser.Operators;
import stone.ast.BlockStmnt;
import stone.ast.IfStmnt;
import stone.ast.Name;
import stone.ast.NegativeExpr;
import stone.ast.NumberLiteral;
import stone.ast.PrimaryExpr;
import stone.ast.StringLiteral;
import stone.ast.While声明类;
import stone.ast.二元表达式类;
import stone.ast.空声明类;
import stone.ast.语法树类;

public class 基本语法分析器类 {

    HashSet<String> reserved = new HashSet<>();
    Operators operators = new Operators();
    Parser expr0 = rule();
    Parser primary = rule(PrimaryExpr.class)
        .or(rule().sep("(").ast(expr0).sep(")"),
            rule().number(NumberLiteral.class),
            rule().identifier(Name.class, reserved),
            rule().string(StringLiteral.class));
    Parser factor = rule().or(rule(NegativeExpr.class).sep("-").ast(primary),
                              primary);
    Parser expr = expr0.expression(二元表达式类.class, factor, operators);

    Parser statement0 = rule();
    Parser block = rule(BlockStmnt.class)
        .sep("{").option(statement0)
        .repeat(rule().sep(";", 词类.EOL).option(statement0))
        .sep("}");
    Parser simple = rule(PrimaryExpr.class).ast(expr);
    Parser statement = statement0.or(
            rule(IfStmnt.class).sep(IfStmnt.关键字_如果).ast(expr).ast(block)
                               .option(rule().sep(IfStmnt.关键字_否则).ast(block)),
            rule(While声明类.class).sep(While声明类.关键字).ast(expr).ast(block),
            simple);

    Parser program = rule().or(statement, rule(空声明类.class))
                           .sep(";", 词类.EOL);

    public 基本语法分析器类() {
        reserved.add(";");
        reserved.add("}");
        reserved.add(词类.EOL);

        operators.add("=", 1, Operators.RIGHT);
        operators.add("==", 2, Operators.LEFT);
        operators.add(">", 2, Operators.LEFT);
        operators.add("<", 2, Operators.LEFT);
        operators.add("+", 3, Operators.LEFT);
        operators.add("-", 3, Operators.LEFT);
        operators.add("*", 4, Operators.LEFT);
        operators.add("/", 4, Operators.LEFT);
        operators.add("%", 4, Operators.LEFT);
    }
    public 语法树类 分析(词法分析器类 lexer) throws 分析例外 {
        return program.parse(lexer);
    }
}
