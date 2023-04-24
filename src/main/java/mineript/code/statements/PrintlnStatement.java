package mineript.code.statements;

import mineript.code.expressions.Expression;

public final class PrintlnStatement implements Statement {

    private final Expression expression;

    public PrintlnStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        System.out.println(expression.eval());
    }

    @Override
    public String toString() {
        return "println " + expression;
    }
}
