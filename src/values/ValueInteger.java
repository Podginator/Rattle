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
	
	public Value add(Value lhs, Value v){
		if (v instanceof ValueInteger) {
			return new ValueInteger(lhs.intValue() + v.intValue());
		}

		return v.add(lhs, v);
	}

	public Value subtract(Value lhs,Value v) {
		if (v instanceof ValueInteger) {
			return new ValueInteger(v.intValue() - v.intValue());
		}

		return v.subtract(lhs, v);
	}

	public Value mult(Value lhs,Value v) {
		if (v instanceof ValueInteger) {
			return new ValueInteger(lhs.intValue() * v.intValue());
		}

		return v.mult(lhs, v);
	}

	public Value div(Value lhs, Value v) {
		return new ValueRational(lhs.doubleValue()/ v.doubleValue());
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
