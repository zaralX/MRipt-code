package mineript.code.statements;

import mineript.code.expressions.Expression;

public final class ForStatement implements Statement {
    private final Statement init;
    private final Expression termination;
    private final Statement increment;
    private final Statement block;

    public ForStatement(Statement init, Expression termination, Statement increment, Statement block) {
        this.init = init;
        this.termination = termination;
        this.increment = increment;
        this.block = block;
    }

    @Override
    public void execute() {
        for (init.execute(); termination.eval().asNumber() != 0; increment.execute()) {
            try {
                block.execute();
            }
            catch (BreakStatement BS) { break; }
            catch (ContinueStatement BS) { continue; }
        }
    }

    @Override
    public String toString() {
        return "for " + init + ", " + termination + ", " + increment + " { " + block + " }";
    }
}
