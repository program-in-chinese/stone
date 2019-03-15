package chap12;
import stone.ast.BlockStmnt;
import stone.ast.ParameterList;
import chap11.ArrayEnv;
import chap11.OptFunction;
import chap6.环境类;

public class OptMethod extends OptFunction {
    OptStoneObject self;
    public OptMethod(ParameterList parameters, BlockStmnt body,
                     环境类 env, int memorySize, OptStoneObject self)
    {
        super(parameters, body, env, memorySize);
        this.self = self;
    }
    @Override public 环境类 makeEnv() {
        ArrayEnv e = new ArrayEnv(size, env);
        e.put(0, 0, self);
        return e;
    }
}
