package chap6;
import stone.*;
import stone.ast.语法树类;
import stone.ast.NullStmnt;

public class BasicInterpreter {
    public static void main(String[] args) throws 分析例外 {
        run(new 基本语法分析器类(), new BasicEnv());
    }
    public static void run(基本语法分析器类 bp, Environment env)
        throws 分析例外
    {
        词法分析器类 lexer = new 词法分析器类(new CodeDialog());
        while (lexer.瞄(0) != 词类.EOF) {
            语法树类 t = bp.分析(lexer);
            if (!(t instanceof NullStmnt)) {
                Object r = ((BasicEvaluator.ASTreeEx)t).eval(env);
                System.out.println("=> " + r);
            }
        }
    }
}
