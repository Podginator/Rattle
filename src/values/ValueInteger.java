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
	public int longValue() {
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
		if (internalValue == v.longValue())
			return 0;
		else if (internalValue > v.longValue())
			return 1;
		else
			return -1;
	}
	
	public Value add(Value v) {
		if (v instanceof ValueInteger) {
			return new ValueInteger(internalValue + v.longValue());
		}

		return v.clone().add(this);
	}

	public Value subtract(Value v) {
		if (v instanceof ValueInteger) {
			return new ValueInteger(internalValue - v.longValue());
		}

		return v.clone().subtract(this);
	}

	public Value mult(Value v) {
		if (v instanceof ValueInteger) {
			return new ValueInteger(internalValue * v.longValue());
		}

		return v.clone().mult(this);
	}

	public Value div(Value v) {
		return new ValueRational(internalValue / v.longValue());
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

	@Override
	public Value clone() {
		return new ValueInteger(internalValue);
	}
}
