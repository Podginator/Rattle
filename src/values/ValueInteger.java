package values;

public class ValueInteger extends ValueAbstract {

	private int internalValue;
	
	public ValueInteger(int b) {
		internalValue = b;
	}
	
	public String getName() {
		return "integer";
	}
	
	/** Convert this to a primitive long. */
	public int intValue() {
		return internalValue;
	}
	
	/** Convert this to a primitive double. */
	public double doubleValue() {
		return (float)internalValue;
	}
	
	/** Convert this to a primitive String. */
	public String stringValue() {
		return "" + internalValue;
	}

	public int compare(Value v) {
		if (internalValue == v.intValue())
			return 0;
		else if (internalValue > v.intValue())
			return 1;
		else
			return -1;
	}
	
	public Value add(Value v){
		if (v instanceof ValueInteger) {
			return new ValueInteger(internalValue + v.intValue());
		}


		return ValueFactory.ConvertValue(v.getClass(), this).add(v);
	}

	public Value subtract(Value v) {
		if (v instanceof ValueInteger) {
			return new ValueInteger(internalValue - v.intValue());
		}


		return ValueFactory.ConvertValue(v.getClass(), this).subtract(v);
	}

	public Value mult(Value v) {
		if (v instanceof ValueInteger) {
			return new ValueInteger(internalValue * v.intValue());
		}

		// This operation is transitive, 2.4*2 == 2*2.4. We also define some other operations with it.
		return v.mult(this);
	}

	public Value div(Value v) {
		return new ValueRational(doubleValue() / v.doubleValue());
	}

	public Value unary_plus() {
		return new ValueInteger(internalValue);
	}

	public Value unary_minus() {
		return new ValueInteger(-internalValue);
	}
	
	public String toString() {
		return "" + internalValue;
	}

}
