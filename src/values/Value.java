package values;

import interpreter.ClassDefinition;

/**
 * An abstract Value, that defines all possible operations on abstract ValueS.
 * <p>
 * If an operation is not supported, throw SemanticException.
 */
public interface Value {

    /**
     * Get name of this Value type.
     */
    String getName();

    /**
     * Perform logical OR on this value and another.
     */
    Value or(Value v);

    /**
     * Perform logical AND on this value and another.
     */
    Value and(Value v);

    /**
     * Perform logical NOT on this value.
     */
    Value not();

    /**
     * Compare this value and another.
     */
    int compare(Value v);

    /**
     * Add this value to another.
     */
    Value add(Value lhs, Value v);

    /**
     * Subtract another value from this.
     */
    Value subtract(Value lhs, Value v);

    /**
     * Multiply this value with another.
     */
    Value mult(Value lhs, Value v);

    /**
     * Divide another value by this.
     */
    Value div(Value lhs, Value v);

    /**
     * Add this value to another.
     */
    Value add(Value v);

    /**
     * Subtract another value from this.
     */
    Value subtract(Value v);

    /**
     * Multiply this value with another.
     */
    Value mult(Value v);

    /**
     * Divide another value by this.
     */
    Value div(Value v);


    /**
     * Return unary plus of this value.
     */
    Value unary_plus();

    /**
     * Return unary minus of this value.
     */
    Value unary_minus();

    /**
     * Convert this to a primitive boolean.
     */
    boolean booleanValue();


    /**
     * Convert this to a primitive double.
     */
    double doubleValue();


    /**
     * Convert to a Primitive int
     * @return integer value of htis.
     */
    int intValue();

    /**
     * Convert this to a primitive string.
     */
    String stringValue();

    /**
     * Convert to a class instance
     * @return The class instance of this object.
     */
    ClassDefinition.ClassInstance objValue();

    /**
     * Convert to a tuple
     */
    Value[] tupleValue();

    /**
     * Test this value and another for equality.
     */
    Value eq(Value v);

    /**
     * Test this value and another for non-equality.
     */
    Value neq(Value v);

    /**
     * Test this value and another for >=
     */
    Value gte(Value v);

    /**
     * Test this value and another for <=
     */
    Value lte(Value v);

    /**
     * Test this value and another for >
     */
    Value gt(Value v);

    /**
     * Test this value and another for <
     */
    Value lt(Value v);


}
