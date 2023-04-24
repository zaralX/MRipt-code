package mineript.code.statements;

import mineript.code.expressions.Expression;
import mineript.code.values.Value;
import mineript.code.values.Variables;

public class AssigmentStatement implements Statement {

    private final String variable;
    private final Expression expression;

    public AssigmentStatement(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void execute() {
        final Value result = expression.eval();
        Variables.set(variable, result);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", variable, expression);
    }
}
