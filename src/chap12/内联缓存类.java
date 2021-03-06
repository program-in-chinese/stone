package chap12;
import java.util.List;
import stone.StoneException;
import stone.ast.语法树类;
import stone.ast.Dot;
import chap6.环境类;
import javassist.gluonj.*;

@Require(对象优化器类.class)
@Reviser public class 内联缓存类 {
    @Reviser public static class DotEx2 extends 对象优化器类.DotEx {
        protected OptClassInfo classInfo = null;
        protected boolean isField;
        protected int index;
        public DotEx2(List<语法树类> c) { super(c); }
        @Override public Object 求值(环境类 env, Object value) {
            if (value instanceof OptStoneObject) {
                OptStoneObject target = (OptStoneObject)value;
                if (target.classInfo() != classInfo)
                    updateCache(target);
                if (isField)
                    return target.read(index);
                else
                    return target.method(index);
            }
            else
                return super.求值(env, value);
        }
        protected void updateCache(OptStoneObject target) {
            String member = name();
            classInfo = target.classInfo();
            Integer i = classInfo.fieldIndex(member);
            if (i != null) {
                isField = true;
                index = i;
                return;
            }
            i = classInfo.methodIndex(member);
            if (i != null) {
                isField = false;
                index = i;
                return;
            }
            throw new StoneException("bad member access: " + member, this);
        }
    }
    @Reviser public static class AssignEx2 extends 对象优化器类.AssignEx {
        protected OptClassInfo classInfo = null;
        protected int index;
        public AssignEx2(List<语法树类> c) { super(c); }
        @Override protected Object setField(OptStoneObject obj, Dot expr,
                                            Object rvalue)
        {
            if (obj.classInfo() != classInfo) {
                String member = expr.name();
                classInfo = obj.classInfo();
                Integer i = classInfo.fieldIndex(member);
                if (i == null)
                    throw new StoneException("bad member access: " + member,
                                             this);       
                index = i;
            }
            obj.write(index, rvalue);
            return rvalue;
        }
    }
}
