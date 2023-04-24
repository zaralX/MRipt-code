package mineript.code.expressions;

import mineript.code.values.NumberValue;
import mineript.code.values.StringValue;
import mineript.code.values.Value;

public final class ConditionalExpression implements Expression {

    public enum Operator {
        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVIDE("/"),

        EQUALS("=="),
        NOTEQUALS("!="),

        LT("<"),
        LTEQUALS("<="),
        GT(">"),
        GTEQUALS(">="),

        AND("&&"),
        OR("||");

        private final String name;

        Operator(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    private final Expression expression1, expression2;
    private final Operator operation;

    public ConditionalExpression(Expression expression1, Expression expression2, Operator operation) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;
    }

    @Override
    public Value eval() {
        final Value value1 = expression1.eval();
        final Value value2 = expression2.eval();

        double number1, number2;
        if (value1 instanceof StringValue) {
            number1 = value1.asString().compareTo(value2.asString());
            number2 = 0;
        } else {
            number1 = value1.asNumber();
            number2 = value2.asNumber();
        }

        boolean result;
        switch (operation) {
            case LT: result = number1 < number2; break;
            case LTEQUALS: result = number1 <= number2; break;
            case GT: result = number1 > number2; break;
            case GTEQUALS: result = number1 >= number2; break;
            case NOTEQUALS: result = number1 != number2; break;

            case AND: result = ( number1 != 0 ) && ( number2 != 0 ); break;

            case OR: result = ( number1 != 0 ) || ( number2 != 0 ); break;

            case EQUALS:
            default:
                result = number1 == number2; break;
        }

        return new NumberValue(result);
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", expression1, operation.getName(), expression2);
    }
}
