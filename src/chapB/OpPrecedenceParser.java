package chapB;
import java.util.Arrays;
import java.util.HashMap;
import stone.*;
import stone.ast.*;

public class OpPrecedenceParser {
    private 词法分析器类 lexer;
    protected HashMap<String,Precedence> operators;

    public static class Precedence {
        int value;
        boolean leftAssoc; // left associative
        public Precedence(int v, boolean a) {
            value = v; leftAssoc = a;
        }
    }
    public OpPrecedenceParser(词法分析器类 p) {
        lexer = p;
        operators = new HashMap<String,Precedence>();
        operators.put("<", new Precedence(1, true));
        operators.put(">", new Precedence(1, true));
        operators.put("+", new Precedence(2, true));
        operators.put("-", new Precedence(2, true));
        operators.put("*", new Precedence(3, true));
        operators.put("/", new Precedence(3, true));
        operators.put("^", new Precedence(4, false));
    }
    public ASTree expression() throws ParseException {
        ASTree right = factor();
        Precedence next;
        while ((next = nextOperator()) != null)
            right = doShift(right, next.value);

        return right;
    }
    private ASTree doShift(ASTree left, int prec) throws ParseException {
        ASTLeaf op = new ASTLeaf(lexer.读());
        ASTree right = factor();
        Precedence next;
        while ((next = nextOperator()) != null && rightIsExpr(prec, next))
            right = doShift(right, next.value);

        return new BinaryExpr(Arrays.asList(left, op, right));
    }
    private Precedence nextOperator() throws ParseException {
        词类 t = lexer.peek(0);
        if (t.isIdentifier())
            return operators.get(t.getText());
        else
            return null;
    }
    private static boolean rightIsExpr(int prec, Precedence nextPrec) {
        if (nextPrec.leftAssoc)
            return prec < nextPrec.value;
        else
            return prec <= nextPrec.value;
    }
    public ASTree factor() throws ParseException {
        if (isToken("(")) {
            token("(");
            ASTree e = expression();
            token(")");
            return e;
        }
        else {
            词类 t = lexer.读();
            if (t.isNumber()) {
                NumberLiteral n = new NumberLiteral(t);
                return n;
            }
            else
                throw new ParseException(t);
        }
    }
    void token(String name) throws ParseException {
        词类 t = lexer.读();
        if (!(t.isIdentifier() && name.equals(t.getText())))
            throw new ParseException(t);
    }
    boolean isToken(String name) throws ParseException {
        词类 t = lexer.peek(0);
        return t.isIdentifier() && name.equals(t.getText());
    }
    public static void main(String[] args) throws ParseException {
        词法分析器类 lexer = new 词法分析器类(new CodeDialog());
        OpPrecedenceParser p = new OpPrecedenceParser(lexer);
        ASTree t = p.expression();
        System.out.println("=> " + t);
    }
}
