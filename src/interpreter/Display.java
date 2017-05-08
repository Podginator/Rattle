package interpreter;

import interpreter.ClassDefinition.ClassInstance;
import values.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * A display manages run-time access to variable and parameter scope where
 * functions may be nested.
 */
class Display implements ClassDefinitionRepository {

    private final int maximumFunctionNesting = 64;
    private FunctionInvocation[] display = new FunctionInvocation[maximumFunctionNesting];
    private Map<String, ClassDefinition> classes = new HashMap<String, ClassDefinition>();
    private int currentLevel;
    private Display outerscope;
    // There are outerscopes, but we might have also called from an inner block, and thus we need to
    // set the calling scope.
    private Display called_scope;
    public static Display globalScope = new Display();

    /**
     * Ctor
     */
    Display() {
        this(null);
    }

    Display(Display outerscope) {
        // root or 0th scope
        currentLevel = 0;
        display = new FunctionInvocation[maximumFunctionNesting];
        display[currentLevel] = new FunctionInvocation(new FunctionDefinition("%main", currentLevel, this));
        this.outerscope = outerscope;
    }

    /**
     * Execute a function in its scope, using a specified parser.
     */
    Value[] execute(FunctionInvocation fn, Parser p) {
        int changeLevel = fn.getLevel();
        FunctionInvocation oldContext = display[changeLevel];
        int oldLevel = currentLevel;


        display[changeLevel] = fn;

        currentLevel = changeLevel;
        Value[] v = display[currentLevel].execute(p);

        display[changeLevel] = oldContext;
        currentLevel = oldLevel;
        return v;
    }

    public Display getOuterScope() {
        return outerscope;
    }

    public void setCalledScope(Display display) {
        called_scope = display;
    }

    /**
     * Get the current scope nesting level.
     */
    int getLevel() {
        return currentLevel;
    }


    Reference findReferenceAtRoot(String name)
    {
        int offset = display[0].findSlotNumber(name);
        if (offset >= 0) {
            return new Reference(0, offset, this);
        }

        return null;
    }


    Reference findReference(String name) {
        return findReference(name, true);
    }

    /**
     * Return a Reference to a variable or parameter.  Return null if it doesn't exist.
     */
    Reference findReference(String name, boolean traverseDown) {
        int level = currentLevel;
        Reference ref = null;

        while (level >= 0) {
            int offset = display[level].findSlotNumber(name);
            if (offset >= 0)
                ref = new Reference(level, offset, this);
            level--;
        }

        // Look through the outerscopes (Should recurse back until we hit global).
        if (ref == null && outerscope != null && traverseDown) {
            ref =  outerscope.findReference(name);
        }

        if (ref == null && called_scope != null && traverseDown) {
            ref = called_scope.findReference(name);
        }

        return ref;
    }

    /**
     * Create a variable in the current level and return its Reference.
     */
    Reference defineVariable(String name) {
        return new Reference(currentLevel, display[currentLevel].defineVariable(name), this);
    }


    FunctionDefinition findFunction(String name) {
        return findFunction(name, true);
    }

    /**
     * Find a function.  Return null if it doesn't exist.
     */
    FunctionDefinition findFunction(String name, boolean traverseDown) {
        int level = currentLevel;

        while (level >= 0) {
            FunctionDefinition definition = display[level].findFunction(name);
            if (definition != null)
                return definition;
            level--;
        }

        // Look through the outerscopes (Should recurse back until we hit global).
        if (outerscope != null && traverseDown) {
            return outerscope.findFunction(name);
        }

        return null;
    }

    /**
     * Find a function in the current level.  Return null if it doesn't exist.
     */
    public FunctionDefinition findFunctionInCurrentLevel(String name) {
        return display[currentLevel].findFunction(name);
    }

    /**
     * Add a function to the current level.
     */
    public void addFunction(FunctionDefinition definition) {
        display[currentLevel].addFunction(definition);
    }

    /**
     * Add a function to the current level.
     */
    public void addClassDefintion(ClassDefinition definition) {
        classes.put(definition.getName(), definition);
    }

    public ClassDefinition getClassDefinition(String name) {
        ClassDefinition def = classes.get(name);

        if (def == null && outerscope != null) {
            def = outerscope.getClassDefinition(name);
        }

        return def;
    }

    /**
     * Return a concrete instance of a class if the definition exists, otherwise null.
     *
     * @return A concrete implementation of the class.
     */
    @Override
    public ClassInstance createClassInstance(String def) {
        ClassDefinition classDef = getClassDefinition(def);
        return classDef != null ? classDef.createInstance() : null;
    }

    @Override
    public boolean classDefinitionExists(ClassDefinition def) {
        return classes.containsKey(def.getName());
    }

    /**
     * Reference to a slot.
     */
    class Reference {
        private int displayDepth;
        private int slotNumber;
        private Display mDisplay;



        /**
         * Ctor
         */
        Reference(int depth, int slot, Display display) {
            displayDepth = depth;
            slotNumber = slot;
            mDisplay = display;
        }


        /**
         * Returns the scope this is part of.
         * @return the scope.
         */
        Display getScope() {
            return mDisplay;
        }

        /**
         * Get value pointed to by this reference.
         */
        Value getValue() {
            return display[displayDepth].getValue(slotNumber);
        }

        /**
         * Set value pointed to by this reference.
         */
        void setValue(Value v) {
            display[displayDepth].setValue(slotNumber, v);
        }
    }


}
