package values;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by podginator on 24/04/2017.
 */
public class ValueTuple extends ValueAbstract  {

    private ArrayList<Value> internalValues;

    public ValueTuple(ArrayList<Value> vals) {
        internalValues = vals;
    }

    public ValueTuple(Value... vals) {
        internalValues = new ArrayList<Value>();
        for (Value val : vals) {
            internalValues.add(val);
        }
    }

    @Override
    public String getName() {
        return "tuple<" + internalValues.stream().map( n -> n.getName() )
                .collect( Collectors.joining( "," ) ) + ">";

    }

    @Override
    public Value add(Value lhs, Value rhs) {
        Value[] lhsVals = lhs.tupleValue();
        Value[] rhsVals = rhs.tupleValue();
        ArrayList<Value> vals = new ArrayList<>();

        for (Value value : lhsVals) {
            vals.add(value);
        }
        for (Value value : rhsVals) {
            vals.add(value);
        }

        return new ValueTuple(vals);
    }

    @Override
    public int compare(Value v) {
        return 0;
    }

    @Override
    public Value[] tupleValue() {
        return internalValues.toArray(new Value[internalValues.size()]);
    }

}
