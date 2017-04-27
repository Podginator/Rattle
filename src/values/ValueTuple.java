package values;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
    public Value add(Value rhs) {
        Value[] rhsVals = rhs.tupleValue();
        ArrayList<Value> vals = (ArrayList<Value>)internalValues.clone();

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
