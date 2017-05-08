package values;

/**
 * Created by podginator on 08/05/2017.
 */
public class ValueNull extends ValueAbstract {
    @Override
    public String getName() {
        return "null";
    }

    @Override
    public int compare(Value v) {

        if (eq(v).booleanValue()) {
            return 0;
        }

        return -1;
    }

    @Override
    public Value eq(Value v) {
        if (v instanceof ValueNull) {
            return new ValueBoolean(true);
        }

        return new ValueBoolean(false);
    }

    @Override
    public Value neq(Value v) {
        if (v instanceof ValueNull) {
            return new ValueBoolean(false);
        }

        return new ValueBoolean(true);
    }
}
