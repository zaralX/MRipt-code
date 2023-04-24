package mineript.code.statements;

import mineript.code.values.Functions;
import mineript.code.values.UserFunction;

import java.util.List;

public final class FunctionDefineStatement implements Statement {

    private final String name;
    private final List<String> args;
    private final Statement body;

    public FunctionDefineStatement(String name, List<String> args, Statement body) {
        this.name = name;
        this.args = args;
        this.body = body;
    }

    @Override
    public void execute() {
        Functions.set(name, new UserFunction(args, body));
    }

    @Override
    public String toString() {
        return "function " + name + " ( " + args.toString() + " ) { " + body.toString() + " }";
    }
}
