package interpreter;

import interpreter.Display.Reference;
import values.Value;
import values.ValueObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Definition of a class.
 */
public class ClassDefinition implements ClassDefinitionRepository, Comparable<ClassDefinition> {

    private String mName;

    private Map<String, Value> mMembers;

    private Set<FunctionDefinition> mMethods;

    private Map<String, ClassDefinition> mInnerClasses;

    private Display mOuterscope;

    private boolean mIsPolymorphic;

    private ClassDefinition mSuperDef;


    /**
     * If the class is polymorphic we need access to the superclasses scope.
     * @param name
     * @param superclass
     */
    public ClassDefinition(String name, ClassDefinition superclass) {
        this(name, superclass.mOuterscope);
        mSuperDef = superclass;
        mIsPolymorphic = true;
    }

    public ClassDefinition(String name, Display outScope) {
        mName = name;
        mMembers = new HashMap<>();
        mMethods = new TreeSet<>();
        mInnerClasses = new HashMap<>();
        mOuterscope = outScope;
    }

    public String getName() {
        return mName;
    }


    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof ClassDefinition) {
            return ((ClassDefinition) arg0).getName().equals(mName);
        }

        return super.equals(arg0);
    }


    /**
     * Since this language is currently dynamically typed, this should suffice.
     *
     * @param name the name of the member variable to add.
     */
    public void defineMember(String name) {
        mMembers.put(name, null);
    }

    /**
     * Define the Member and set a default
     *
     * @param name the name.
     * @param val  a default value.
     */
    public void defineMemberAndSet(String name, Value val) {
        mMembers.put(name, val);
    }

    /**
     * The name of the method to add.
     *
     * @param fnDef the function definition to add
     */
    public void addMethod(FunctionDefinition fnDef) {
        mMethods.add(fnDef);
    }

    /**
     * Return an instance of a class.
     *
     * @return
     */
    public ClassInstance createInstance() {
        if (mIsPolymorphic) {
            return new ClassInstance(this, mSuperDef);
        }

        return new ClassInstance(this);
    }

    @Override
    public void addClassDefintion(ClassDefinition definition) {
        mInnerClasses.put(definition.getName(), definition);
    }

    @Override
    public boolean classDefinitionExists(ClassDefinition def) {
        return mInnerClasses.containsKey(def.getName());
    }

    @Override
    public int compareTo(ClassDefinition arg0) {
        return mName.compareTo(arg0.mName);
    }

    public ClassDefinition getClassDefinition(String name) {
        ClassDefinition def = mInnerClasses.get(name);

        if (def == null && mOuterscope != null) {
            def = mOuterscope.getClassDefinition(name);
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

    public static class ClassInstance {

        /**
         * Display, or independent scoping for it. Variables should be scoped entirely to an object
         */
        private Display mDisplay;

        private ClassInstance(ClassDefinition def) {
            mDisplay = new Display(def.mOuterscope);
            /** add the members and scope to each variable */
            def.mMembers.forEach((e, v) -> {
                mDisplay.defineVariable(e).setValue(v);
            });
            def.mMethods.forEach(e -> {
                // Set the method scope to be the classes scope.
                e.setScope(mDisplay);
                mDisplay.addFunction(e);
            });
            def.mInnerClasses.forEach((e, v) -> {
                mDisplay.addClassDefintion(v);
            });

            // We want to define a 'this' variable, as we'll want the ability to check for variables out of scope.
            mDisplay.defineVariable("this").setValue(new ValueObject(this));
        }


        /**
         * Polymorphic Constructor
         * @param def The Class we want to create
         * @param superDef the Super Class.
         */
        private ClassInstance(ClassDefinition def, ClassDefinition superDef){
            ClassInstance superClass = superDef.createInstance();
            mDisplay = new Display(superClass.getScope());


            def.mMethods.forEach(e -> {
                e.setScope(mDisplay);
                mDisplay.addFunction(e);
            });

            def.mInnerClasses.forEach((e, v) -> {
                mDisplay.addClassDefintion(v);
            });


            /** add the members and scope to each variable */
            def.mMembers.forEach((e, v) -> {
                Reference oldRef = superClass.getScope().findReference(e, false);
                mDisplay.defineVariable(e).setValue(v);

                if (oldRef != null) {
                    // Is this sufficient? We want all the References to store a
                    // ptr to the new Values. I think this is how java works.
                    oldRef.setValue(v);
                }
            });

            //Expose the superclass
            mDisplay.defineVariable("super").setValue(new ValueObject(superClass));
            mDisplay.defineVariable("this").setValue(new ValueObject(this));

        }

        /**
         * Returns a member if it exists, otherwise return null.
         *
         * @param name the name of the member to return.
         * @return The value of the member.
         */
        Reference getMember(String name) {
            Reference ref = mDisplay.findReferenceAtRoot(name);

            // == is correct here as we're checking the memlocation not that they are semantically the same.
            if (ref == null || ref.getScope() == Display.globalScope){
                return null;
            }

            return ref;
        }



        /**
         * @param name
         * @param val
         */
        void setMember(String name, Value val) {
            Reference ref = mDisplay.findReference(name, false);
            if (ref != null) {
                ref.setValue(val);
            } else {
                //Throw a hissy fit?
            }
        }


        /**
         * Find a function.  Return null if it doesn't exist.
         */
        FunctionDefinition findMethod(String name) {
            FunctionDefinition def = mDisplay.findFunction(name);

            // If we've returned null, or if we are in the outmost scope, then return null.
            if (def == null || def.getScope().getOuterScope() == null) {
                return null;
            }

            return def;
        }


        /**
         * Execute within the scope of the class.
         *
         * @param fn the function
         * @param p  the parser
         * @return the value it returns.
         */
        Value[] executeMethod(FunctionInvocation fn, Parser p) {
            return mDisplay.execute(fn, p);
        }



        Display getScope() {
            return mDisplay;
        }


        Display getSuperScope() { return mDisplay.getOuterScope(); }

    }


}


