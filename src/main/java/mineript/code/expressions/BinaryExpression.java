package mineript.code.expressions;

import mineript.code.values.ArrayValue;
import mineript.code.values.NumberValue;
import mineript.code.values.StringValue;
import mineript.code.values.Value;

public final class BinaryExpression implements Expression {
    private final Expression expression1, expression2;
    private final char operation;

    public BinaryExpression(Expression expression1, Expression expression2, char operation) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;
    }

    @Override
    public Value eval() {
        final Value value1 = expression1.eval();
        final Value value2 = expression2.eval();
        if (value1 instanceof StringValue || value1 instanceof ArrayValue) {
            final String string1 = value1.asString();
            switch (operation) {
                case '*':
                    final StringBuilder buffer = new StringBuilder();
                    for (int i = 0; i < (int) value2.asDouble(); i++) {
                        buffer.append(string1);
                    }
                    return new StringValue(buffer.toString());
                case '+':
                default:
                    return new StringValue(string1 + value2.toString());
            }
        }

        final double number_one = value1.asDouble();
        final double number_two = value2.asDouble();
        switch (operation) {
            case '*':
                return new NumberValue(number_one * number_two);
            case '/':
                return new NumberValue(number_one / number_two);
            case '-':
                return new NumberValue(number_one - number_two);
            case '+':
            default:
                return new NumberValue(number_one + number_two);
        }
    }

    @Override
    public String toString() {
        return String.format("(%s %c %s)", expression1, operation, expression2);
    }
}
