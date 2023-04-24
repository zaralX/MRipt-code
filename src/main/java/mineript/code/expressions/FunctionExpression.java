package mineript.code.expressions;

import mineript.code.values.*;

import java.util.ArrayList;
import java.util.List;

public final class FunctionExpression implements Expression {

    private final String name;
    private final List<Expression> args;

    public FunctionExpression(String name, List<Expression> args) {
        this.name = name;
        this.args = args;
    }

    public FunctionExpression(String name) {
        this.name = name;
        this.args = new ArrayList<>();
    }

    public void add(Expression arg) {
        args.add(arg);
    }

    @Override
    public Value eval() {
        final int size = args.size();
        final Value[] values = new Value[size];
        for (int i = 0; i < size; i++) {
            values[i] = args.get(i).eval();
        }

        final Function function = Functions.get(name);
        if (function instanceof UserFunction) {
            final UserFunction userFunction = (UserFunction) function;
            if (size != userFunction.argsCount()) throw new RuntimeException("Args count mismatch!");

            Variables.push();

            for (int i = 0; i < size; i++) {
                Variables.set(userFunction.getArg(i), values[i]);
            }

            final Value result = userFunction.execute(values);
            Variables.pop();
            return result;
        }
        return function.execute(values);
    }

    @Override
    public String toString() {
        return name + "(" + args.toString() + ")";
    }
}
