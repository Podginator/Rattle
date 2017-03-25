package values;

import interpreter.ClassDefinition;
import interpreter.ClassDefinition.ClassInstance;

public class ValueObject extends ValueAbstract {


    private ClassDefinition.ClassInstance internalMember;


    public ValueObject(ClassDefinition.ClassInstance instance) {
        internalMember = instance;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int compare(Value v) {
        return 0;
    }

    @Override
    public ClassInstance objValue() {
        return internalMember;
    }
}
