package chap14;
import java.util.List;
import javassist.gluonj.*;
import stone.ast.*;
import chap11.环境优化器类;
import chap11.Symbols;
import chap11.环境优化器类.ASTreeOptEx;
import chap6.Environment;
import chap6.基本求值器类.ASTreeEx;

@Require(环境优化器类.class)
@Reviser public class TypedEvaluator {
    @Reviser public static class DefStmntEx extends 环境优化器类.DefStmntEx {
        public DefStmntEx(List<语法树类> c) { super(c); }
        public TypeTag type() { return (TypeTag)子(2); }
        @Override public BlockStmnt body() { return (BlockStmnt)子(3); }
        @Override public String toString() {
            return "(def " + name() + " " + parameters() + " " + type() + " "
                   + body() + ")";
        }
    }
    @Reviser public static class ParamListEx extends 环境优化器类.ParamsEx {
        public ParamListEx(List<语法树类> c) { super(c); }
        @Override public String name(int i) {
            return ((语法树叶类)子(i).子(0)).词().getText();
        }
        public TypeTag typeTag(int i) {
            return (TypeTag)子(i).子(1);
        }
    }
    @Reviser public static class VarStmntEx extends VarStmnt {
        protected int index;
        public VarStmntEx(List<语法树类> c) { super(c); }
        public void lookup(Symbols syms) {
            index = syms.putNew(name());
            ((ASTreeOptEx)initializer()).lookup(syms);
        }
        public Object eval(Environment env) {
            Object value = ((ASTreeEx)initializer()).eval(env);
            ((环境优化器类.EnvEx2)env).put(0, index, value);
            return value;
        }
    }
}
