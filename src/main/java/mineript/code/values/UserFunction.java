package mineript.code.values;

import mineript.code.statements.ReturnStatement;
import mineript.code.statements.Statement;

import java.util.List;

public final class UserFunction implements Function {

    private final List<String> args;
    private final Statement body;

    public UserFunction(List<String> args, Statement body) {
        this.args = args;
        this.body = body;
    }

    public int argsCount() {
        return args.size();
    }

    public String getArg(int index) {
        if (index < 0 || index >= argsCount()) {
            return "";
        }
        return args.get(index);
    }

    @Override
    public Value execute(Value... args) {
        try {
            body.execute();
            return NumberValue.ZERO;
        } catch (ReturnStatement RT) {
            return RT.getResult();
        }

    }
}
