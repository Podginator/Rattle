package values;

public class ValueString extends ValueAbstract {

    private String internalValue;

    public ValueString(String b) {
        internalValue = b;
    }

    /**
     * Return a ValueString given a quote-delimited source string.
     */
    public static ValueString stripDelimited(String b) {
        return new ValueString(b.substring(1, b.length() - 1));
    }

    public String getName() {
        return "string";
    }

    /**
     * Convert this to a String.
     */
    public String stringValue() {
        return internalValue;
    }

    public int compare(Value v) {
        return internalValue.compareTo(v.stringValue());
    }

    /**
     * Add performs string concatenation.
     */
    public Value add(Value lhs, Value rhs) {
        return new ValueString(lhs.stringValue() + rhs.stringValue());
    }

    public String toString() {
        return internalValue;
    }

}
