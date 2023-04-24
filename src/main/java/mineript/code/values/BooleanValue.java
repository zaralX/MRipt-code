package mineript.code.values;

public class BooleanValue implements Value {
    private final Boolean value;

    public BooleanValue(Boolean value) {
        this.value = value;
    }

    @Override
    public double asDouble() {
        return value ? 1.0 : 0.0;
    }
    @Override
    public int asNumber() {
        return value ? 1 : 0;
    }
    @Override
    public boolean asBoolean() {
        return value;
    }


    @Override
    public String asString() {
        return value ? "true" : "false";
    }

    @Override
    public String toString() {
        return asString();
    }
}
