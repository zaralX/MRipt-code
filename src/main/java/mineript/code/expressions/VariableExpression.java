package mineript.code.expressions;

import mineript.code.values.Value;
import mineript.code.values.Variables;

public class VariableExpression implements Expression {
    private final String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        if (!Variables.isExists(name)) throw new RuntimeException("Constant "+name+" not exists");
        return Variables.get(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
