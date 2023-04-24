package mineript.code.statements;

import mineript.code.expressions.Expression;

public final class WhileStatement implements Statement {

    private final Expression condition;
    private final Statement statement;

    public WhileStatement(Expression condition, Statement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public void execute() {
        while (condition.eval().asBoolean()) {
            try {
                statement.execute();
            }
            catch (BreakStatement BS) { break; }
            catch (ContinueStatement BS) { continue; }
        }
    }

    @Override
    public String toString() {
        return "while " + condition + " " + statement;
    }
}
