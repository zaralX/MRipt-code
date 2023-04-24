package mineript.code.statements;

import mineript.code.expressions.FunctionExpression;

public final class FunctionStatement implements Statement {
    private final FunctionExpression function;

    public FunctionStatement(FunctionExpression function) {
        this.function = function;
    }

    @Override
    public void execute() {
        function.eval();
    }

    @Override
    public String toString() {
        return function.toString();
    }
}
