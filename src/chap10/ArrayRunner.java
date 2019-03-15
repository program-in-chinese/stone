package chap10;
import javassist.gluonj.util.Loader;
import chap7.闭包求值器类;
import chap8.原生求值器类;
import chap9.ClassEvaluator;
import chap9.ClassInterpreter;

public class ArrayRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(ClassInterpreter.class, args, ClassEvaluator.class,
                   ArrayEvaluator.class, 原生求值器类.class,
                   闭包求值器类.class);
    }
}
