package values;

import interpreter.ExceptionSemantic;
import interpreter.FunctionDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by podginator on 16/03/2017.
 */
public class ValueFunction extends ValueAbstract {

    List<FunctionDefinition> mFunctionDef;

    /**
     * Constructor
     * @param fnDef takes a function Def
     */
    public ValueFunction(FunctionDefinition... fnDef) {
        mFunctionDef = Arrays.stream(fnDef).collect(Collectors.toList());
    }


    public List<FunctionDefinition> getDefintion() {
        return mFunctionDef;
    }


    public Value add(Value v) {
        if (!(v instanceof ValueFunction)) {
            throw new ExceptionSemantic("Cannot curry non-functions with other a function");
        }

        List<FunctionDefinition> comb = new ArrayList<>();
        comb.addAll(mFunctionDef);
        comb.addAll(((ValueFunction) v).getDefintion());

        return new ValueFunction( comb.toArray(new FunctionDefinition[comb.size()]) );
    }


    @Override
    public String getName() {
        return "Lambda Chain";
    }

    @Override
    public int compare(Value v) {
        return 0;
    }

    public String toString() {
        return "Lambda";
    }

}
