package mineript.code.expressions;

import mineript.code.values.ArrayValue;
import mineript.code.values.Value;
import mineript.code.values.Variables;

import java.util.List;

public final class ArrayAccessExpression implements Expression {

    private final String variable;
    private final List<Expression> indexes;

    public ArrayAccessExpression(String variable, List<Expression> indexes) {
        this.variable = variable;
        this.indexes = indexes;
    }

    @Override
    public Value eval() {
        return getArray().get(lastIndex());
    }

    public int lastIndex() {
        return index(indexes.size()-1);
    }

    private int index(int index) {
        return indexes.get(index).eval().asNumber();
    }

    public ArrayValue getArray() {
        ArrayValue array = consumeArray(Variables.get(variable));
        final int last = indexes.size() - 1;
        for (int i = 0; i < last; i++) {
            array = consumeArray( array.get( index(i) ) );
        }
        return array;
    }

    private ArrayValue consumeArray(Value value) {
        if (value instanceof ArrayValue) {
            return (ArrayValue) value;
        } else {
            throw new RuntimeException("Array expected");
        }
    }

    @Override
    public String toString() {
        return variable + indexes;
    }
}
