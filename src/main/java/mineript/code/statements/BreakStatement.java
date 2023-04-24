package mineript.code.statements;

public final class BreakStatement extends RuntimeException implements Statement{
    @Override
    public void execute() {
        throw this;
    }

    @Override
    public String toString() {
        return "break";
    }
}
