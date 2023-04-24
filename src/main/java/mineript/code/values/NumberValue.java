package mineript.code.values;

public class NumberValue implements Value {

    public static final NumberValue ZERO = new NumberValue(0);
    private final double value;

    public NumberValue(boolean value) {
        this.value = value ? 1 : 0;
    }

    public NumberValue(double value) {
        this.value = value;
    }

    @Override
    public double asDouble() {
        return value;
    }

    @Override
    public int asNumber() {
        return (int) value;
    }

    @Override
    public boolean asBoolean() {
        return value != 0;
    }

    @Override
    public String asString() {
        return Double.toString(value);
    }

    @Override
    public String toString() {
        return asString();
    }
}
