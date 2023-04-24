package mineript.code.expressions;

import mineript.code.values.NumberValue;
import mineript.code.values.Value;

public class UnaryExpression implements Expression {
    private final Expression expression1;
    private final char operation;

    public UnaryExpression(char operation, Expression expression1) {
        this.expression1 = expression1;
        this.operation = operation;
    }

    @Override
    public Value eval() {
        switch (operation) {
            case '-':
                return new NumberValue(-expression1.eval().asDouble());
            case '+':
            default:
                return expression1.eval();
        }
    }

    @Override
    public String toString() {
        return String.format("%c %s", operation, expression1);
    }
}
