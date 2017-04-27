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

    public int intValue() {
        int retVal = -1;
        try {
            retVal = Integer.parseInt(internalValue);
        } catch (NumberFormatException e) {
            return super.intValue();
        }

        return retVal;
    }

    public int compare(Value v) {
        return internalValue.compareTo(v.stringValue());
    }

    /**
     * Add performs string concatenation.
     */
    @Override
    public Value add(Value rhs) {
        return new ValueString(stringValue() + rhs.stringValue());
    }


    public Value mult(Value rhs) {
        String newStr = "";
        for (int i = 0; i < rhs.intValue(); i++) {
            newStr += internalValue;
        }
        return new ValueString(newStr);
    }

    public String toString() {
        return internalValue;
    }

}
