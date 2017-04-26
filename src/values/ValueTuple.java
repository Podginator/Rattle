//package values;
//
//import java.util.ArrayList;
//
///**
// * Created by podginator on 24/04/2017.
// */
//public class ValueTuple extends ValueAbstract  {
//
//    private ArrayList<Value> internalValues;
//
//    public ValueTuple(Value... vals) {
//        internalValues = new ArrayList<Value>();
//        for (Value val : vals) {
//            internalValues.add(val);
//        }
//    }
//
//    /**
//     * Return a ValueString given a quote-delimited source string.
//     */
//    public static ValueString stripDelimited(String b) {
//        return new ValueString(b.substring(1, b.length() - 1));
//    }
//
//    public String getName() {
//        return "string";
//    }
//
//    /**
//     * Convert this to a String.
//     */
//    public String stringValue() {
//        return null;
//    }
//
//
//    /**
//     * Add performs string concatenation.
//     */
//    public Value add(Value v) {
//        return new ValueString(null + v.stringValue());
//    }
//
//    public String toString() {
//        return null;
//    }
//
//    @Override
//    public Value clone() {
//        return new ValueString(null);
//    }
//
//
//}
