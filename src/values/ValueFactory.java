package values;
import interpreter.ClassDefinition;
import interpreter.ExceptionSemantic;

/**
 *  I dislike this but it does handle situations where
 *  You're going trying to add/mult etc to incongruent values.
 */
public class ValueFactory {

    public static Value CreateValue(Object obj) {
        if (obj instanceof String) {
            return new ValueString((String) obj);
        } else if (obj instanceof Double) {
            return new ValueRational((Double) obj);
        } else if (obj instanceof Integer) {
            return new ValueInteger((Integer) obj);
        } else if (obj instanceof Value[]) {
            return new ValueTuple((Value[]) obj);
        } else if (obj instanceof Boolean) {
            return new ValueBoolean((Boolean) obj);
        } else if (obj instanceof ClassDefinition.ClassInstance) {
            return new ValueObject((ClassDefinition.ClassInstance) obj);
        }

        throw new ExceptionSemantic("No conversion exists from a " + obj.getClass() + " to a Value");
    }

    public static <T extends Value> Value ConvertValue(Class<T> tClass, Value convertFrom) {

        Object obj;
        Class classType = tClass;

        if (classType.equals(ValueInteger.class)) {
            obj = convertFrom.intValue();
        } else if (classType.equals(ValueRational.class)) {
            obj = convertFrom.doubleValue();
        } else if (classType.equals(ValueObject.class)) {
            obj = convertFrom.objValue();
        } else if (classType.equals(ValueString.class)) {
            obj = convertFrom.stringValue();
        } else if (classType.equals(ValueTuple.class)) {
            obj = convertFrom.tupleValue();
        } else {
            throw new ExceptionSemantic("Cannot convert from " + convertFrom.getClass().getTypeName() + " to " + classType.getTypeName());
        }

        return CreateValue(obj);
    }
}
