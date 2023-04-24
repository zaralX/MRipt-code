package mineript.code.statements;

import mineript.code.expressions.Expression;
import mineript.code.values.Value;

public final class ReturnStatement extends RuntimeException implements Statement {

    private final Expression expression;
    private Value result;

    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }

    public Value getResult() {
        return result;
    }

    @Override
    public void execute() {
        result = expression.eval();
        throw this;
    }

    @Override
    public String toString() {
        return "return";
    }
}
