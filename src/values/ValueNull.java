package values;

/**
 * Created by podginator on 24/04/2017.
 */
public class ValueNull extends ValueAbstract{
    @Override
    public String getName() {
        return "NULL";
    }

    @Override
    public int compare(Value v) {
        return 0;
    }
}
