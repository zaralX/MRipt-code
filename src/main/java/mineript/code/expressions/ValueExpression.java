package mineript.code.expressions;

import mineript.code.values.BooleanValue;
import mineript.code.values.NumberValue;
import mineript.code.values.StringValue;
import mineript.code.values.Value;

public final class ValueExpression implements Expression {
    private final Value value;

    public ValueExpression(double value) {
        this.value = new NumberValue(value);
    }
    public ValueExpression(String value) {
        this.value = new StringValue(value);
    }
    public ValueExpression(Boolean value) {
        this.value = new BooleanValue(value);
    }

    @Override
    public Value eval() {
        return value;
    }

    @Override
    public String toString() {
        return value.asString();
    }
}
