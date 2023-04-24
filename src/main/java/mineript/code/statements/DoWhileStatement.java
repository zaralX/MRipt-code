package mineript.code.statements;

import mineript.code.expressions.Expression;

public final class DoWhileStatement implements Statement {

    private final Expression condition;
    private final Statement statement;

    public DoWhileStatement(Expression condition, Statement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public void execute() {
        do {
            try {
                statement.execute();
            }
            catch (BreakStatement BS) { break; }
            catch (ContinueStatement BS) { continue; }
        }
        while (condition.eval().asBoolean());
    }

    @Override
    public String toString() {
        return "do " + statement + " while " + condition;
    }
}
