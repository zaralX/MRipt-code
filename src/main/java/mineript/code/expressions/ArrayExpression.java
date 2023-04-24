package mineript.code.expressions;

import mineript.code.values.ArrayValue;
import mineript.code.values.Value;

import java.util.List;

public final class ArrayExpression implements Expression {

    private final List<Expression> args;

    public ArrayExpression(List<Expression> args) {
        this.args = args;
    }

    @Override
    public Value eval() {
        final int size = args.size();
        final ArrayValue array = new ArrayValue(size);
        for (int i = 0; i < size; i++) {
            array.set(i, args.get(i).eval());
        }
        return array;
    }

    @Override
    public String toString() {
        return args.toString();
    }
}
