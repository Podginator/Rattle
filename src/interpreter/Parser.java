package interpreter;

import interpreter.ClassDefinition.ClassInstance;
import interpreter.Display.Reference;
import jdk.nashorn.internal.runtime.ParserException;
import parser.*;
import values.*;
import values.WritableValues.ValueFile;
import values.WritableValues.ValueSocket;
import values.WritableValues.Writable;
import values.WritableValues.std_out;

import java.util.ArrayList;
import java.util.List;

public class Parser implements RattleVisitor {


    // Scope display handler
    private Display scope = Display.globalScope;

    // Get the ith child of a given node.
    private static SimpleNode getChild(SimpleNode node, int childIndex) {
        return (SimpleNode) node.jjtGetChild(childIndex);
    }

    // Get the token value of the ith child of a given node.
    private static String getTokenOfChild(SimpleNode node, int childIndex) {
        return getChild(node, childIndex).tokenValue;
    }

    // Execute a given child of a given node, and return its value as a Value.
    // This is used by the expression evaluation nodes.
    Value doChild(SimpleNode node, int childIndex) {
        try {
            return (Value) doChild(node, childIndex, null);
        } catch (ClassCastException e) {
            System.out.println("Expression returns multiple values. Must index to use as single Value");
            System.exit(1);
        }
        return null;
    }

    // Execute a given child of the given node
    public Object doChild(SimpleNode node, int childIndex, Object data) {
        return node.jjtGetChild(childIndex).jjtAccept(this, data);
    }

    Value[] doChildMulti(SimpleNode node, int childIndex, Object data) {
        Object childRes = node.jjtGetChild(childIndex).jjtAccept(this, data);

        if (childRes instanceof Value[]) {
            return (Value[]) childRes;
        }

        return ((Value) childRes).tupleValue();
    }

    // Execute all children of the given node
    Object doChildren(SimpleNode node, Object data) {
        return node.childrenAccept(this, data);
    }


    Value[] doChildrenMulti(SimpleNode node, Object data) {
        ArrayList<Value> val = new ArrayList<>();

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {

            Value[] vals = doChildMulti(node, i, data);

            for (Value value : vals) {
                val.add(value);
            }
        }
        return val.toArray(new Value[val.size()]);
    }

    // Called if one of the following methods is missing...
    public Object visit(SimpleNode node, Object data) {
        System.out.println(node + ": acceptor not implemented in subclass?");
        return data;
    }

    // Execute a Sili program
    public Object visit(ASTCode node, Object data) {
        return doChildren(node, data);
    }

    // Execute a statement
    public Object visit(ASTStatement node, Object data) {
        return doChildren(node, data);
    }

    // Execute a block
    public Object visit(ASTBlock node, Object data) {
        return doChildren(node, data);
    }


    // Function definition
    public Object visit(ASTFnDef node, Object data) {

        String fnname = getTokenOfChild(node, 0);
        if (scope.findFunctionInCurrentLevel(fnname) != null)
            throw new ExceptionSemantic("Function " + fnname + " already exists.");

        FunctionDefinition currentFunctionDefinition = new FunctionDefinition(fnname, scope.getLevel() + 1, scope);
        // Child 1 - function definition parameter list
        doChild(node, 1, currentFunctionDefinition);

        //Only add to scope if a FUNCTION not METHOD.


        if (!node.isObject) {
            // Add to available functions
            scope.addFunction(currentFunctionDefinition);
        } else {
            ((ClassDefinition) data).addMethod(currentFunctionDefinition);
        }
        // Child 2 - function body
        currentFunctionDefinition.setFunctionBody(getChild(node, 2));


        // Child 3 - optional return expression
        if (node.fnHasReturn)
            currentFunctionDefinition.setFunctionReturnExpression(getChild(node, 3));

        // Preserve this definition for future reference, and so we don't define
        // it every time this node is processed.
        return data;
    }

    // Function definition parameter list
    public Object visit(ASTParmlist node, Object data) {
        FunctionDefinition currentDefinition = (FunctionDefinition) data;
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            currentDefinition.defineParameter(getTokenOfChild(node, i));
        return data;
    }

    // Function body
    public Object visit(ASTFnBody node, Object data) {
        return doChildren(node, data);
    }


    // Function return expression
    public Object visit(ASTReturnExpression node, Object data) {
        return doChildrenMulti(node, data);
    }

    // Function invocation in an expression
    public Object visit(ASTFnInvoke node, Object data) {
        List<FunctionDefinition> fndefs = new ArrayList<>();

        // Child 0 - identifier (fn name)
        String fnname = getTokenOfChild(node, 0);
        FunctionDefinition def = scope.findFunction(fnname);

        // Check for lambda, this should be done after we see if a function by that name exists, I think.
        if (def == null) {
            //Check to see if a variable contains a lambda.
            Reference fnRef = scope.findReference(fnname, true);

            if (fnRef != null && fnRef.getValue() instanceof ValueFunction) {
                fndefs.addAll(((ValueFunction) fnRef.getValue()).getDefintion());
            }
        } else {
            fndefs.add(def);
        }

        if (fndefs.size() == 0)
            throw new ExceptionSemantic("Function " + fnname + " is undefined.");

        FunctionInvocation newInvocation = new FunctionInvocation(fndefs.get(0));
        doChild(node, 1, newInvocation);
        Value[] retVal = fndefs.get(0).hasReturn() ? scope.execute(newInvocation, this) : null;

        // Then do the rest.
        if (fndefs.size() > 1) {
            for (int i = 1; i < fndefs.size(); i++) {
                final Value[] rets = retVal;
                final int j = i;
                retVal = (Value[]) PerformInScope(fndefs.get(i).getScope(), scope, () -> {
                    FunctionInvocation newInv = new FunctionInvocation(fndefs.get(j));
                    for (Value value : rets) {
                        newInv.setArgument(value);
                    }
                    return scope.execute(newInv, this);
                });
            }
        }

        if (retVal != null) {
            return retVal.length == 1 ? retVal[0] : retVal;
        }
        return data;
    }

    // Function invocation argument list.
    public Object visit(ASTArgList node, Object data) {
        FunctionInvocation newInvocation = (FunctionInvocation) data;
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            Value[] args = doChildMulti(node, i, null);
            for (Value arg : args) {
                newInvocation.setArgument(arg);
            }
        }
        newInvocation.checkArgumentCount();
        return data;
    }

    // Execute an IF
    public Object visit(ASTIfStatement node, Object data) {
        // evaluate boolean expression
        Value hopefullyValueBoolean = doChild(node, 0);
        if (!(hopefullyValueBoolean instanceof ValueBoolean))
            throw new ExceptionSemantic("The test expression of an if statement must be boolean.");

        PerformInNewScope(() -> {
            if (((ValueBoolean) hopefullyValueBoolean).booleanValue())
                doChild(node, 1, null);                            // if(true), therefore do 'if' statement
            else if (node.ifHasElse)                        // does it have an else statement?
                doChild(node, 2, null);                            // if(false), therefore do 'else' statement

            return null;
        });
        return data;

    }

    // Execute a FOR loop
    public Object visit(ASTForLoop node, Object data) {
        PerformInNewScope(() -> {
            doChild(node, 0, null);
            while (true) {
                // evaluate loop test
                Value hopefullyValueBoolean = doChild(node, 1);
                if (!(hopefullyValueBoolean instanceof ValueBoolean))
                    throw new ExceptionSemantic("The test expression of a for loop must be boolean.");
                if (!((ValueBoolean) hopefullyValueBoolean).booleanValue())
                    break;
                // do loop statement
                doChild(node, 3, null);
                // assign loop increment
                doChild(node, 2, null);
            }

            return null;
        });
        return data;
    }

    // Process an identifier
    // This doesn't do anything, but needs to be here because we need an ASTIdentifier node.
    public Object visit(ASTIdentifier node, Object data) {
        return data;
    }

    @Override
    public Object visit(ASTMultiAssignment node, Object data) {
        Display.Reference reference = null;

        int indexOfExpressions;

        if (node.optimized != null) {
            indexOfExpressions = (Integer) node.optimized;
        } else {
            indexOfExpressions = getIndexOfExpressionsInAssignment(node);
            node.optimized = new Integer(indexOfExpressions);
        }

        // Iterate over all of them, give them values.
        int i = 0;
        int j = 0;
        while (i < indexOfExpressions) {
            Value[] vals;

            try {
                vals = doChildMulti(node, indexOfExpressions + j, null);
            } catch (IndexOutOfBoundsException e) {
                throw new ParserException("Too few expressions to multiply assign");
            }


            for (Value val : vals) {
                SimpleNode child = getChild(node, i);

                if (child.optimized != null) {
                    reference = (Display.Reference) (child.optimized);
                } else {

                    if (child.isObject) {
                        reference = getReferenceFromMember((ASTMemIdentifier) child);
                    } else {
                        String name = child.tokenValue;
                        reference = scope.findReference(name);

                        if (reference == null) {
                            reference = scope.defineVariable(name);
                        }
                    }

                    child.optimized = reference;
                }
                reference.setValue(val);
                i++;
            }
            j++;
        }

        return data;
    }

    @Override
    public Object visit(ASTMemIdentifier node, Object data) {
        return null;
    }


    // Execute the WRITE statement
    public Object visit(ASTWrite node, Object data) {

        Value[] vals = doChildMulti(node, 0, null);
        boolean first = true;
        Writable writer = null;


        int node_num_children = node.jjtGetNumChildren();
        if (node_num_children >= 2) {
            boolean isAppend = false;
            if (node_num_children == 3) {
                Value val = doChild(node, 3);
                isAppend = val.stringValue().equals("-a");
            }
            Value val = doChild(node, 1);
            if (val instanceof Writable) {
                writer = (Writable) val;
            }
        } else {
            writer = new std_out();
        }

        String resultString = "";

        for (Value val : vals) {
            if (!first) {
                resultString += (", ");
            }
            resultString += (val.toString());
            first = false;
        }
        resultString += "\n";

        writer.write(resultString);
        return data;
    }

    @Override
    public Object visit(ASTNULL node, Object data) {
        return new ValueNull();
    }

    // Dereference a variable or parameter, and return its value.
    public Object visit(ASTDereference node, Object data) {

        SimpleNode child = getChild(node, 0);
        String name = getTokenOfChild(node, 0);
        Display.Reference reference = scope.findReference(name);

        if (reference == null)
            throw new ExceptionSemantic("Variable or parameter " + name + " is undefined.");

        // If we're an object deref, try to get the internal member value.
        if (node.isObject) {

            for (int i = 0; i < child.jjtGetNumChildren(); i++) {

                ClassInstance instance = reference.getValue().objValue();
                String memName = getTokenOfChild(child, i);
                reference = instance.getMember(memName);

                // If the reference is the global scope we're not referring to a member anymore.
                if (reference == null) {
                    throw new ExceptionSemantic("member of " + name + ", " + memName + "  is undefined.");
                }
            }
        }
        node.optimized = reference;

        return reference.getValue();
    }

    private Reference getReferenceFromMember(ASTMemIdentifier id) {
        Reference res = scope.findReference(id.tokenValue);

        if (res != null) {
            for (int i = 0; i < id.jjtGetNumChildren(); i++) {
                ClassInstance obj = ((ValueObject) res.getValue()).objValue();
                String memName = getTokenOfChild(id, i);
                res = obj.getMember(memName);
                if (res == null) {
                    throw new ExceptionSemantic("Cannot Find member" + memName + " in object " + id.tokenValue);
                }

            }
        }

        return res;
    }

    private int getIndexOfExpressionsInAssignment(ASTMultiAssignment node) {

        // Iterate through all the nodes.
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            // When the node is no longer an Identifier or Member_Identifer, end.
            SimpleNode child = getChild(node, i);
            if (!(child instanceof ASTIdentifier || child instanceof ASTMemIdentifier)) {
                return i;
            }
        }


        return -1;

    }

    @Override
    public Object visit(ASTIndexedExpression node, Object data) {
        int index = doChild(node, 1).intValue();
        Value[] val = doChildMulti(node, 0, data);
        return val != null && val.length >= (index + 1) ? val[index] : new ValueNull();
    }

    // Execute an assignment statement.
    public Object visit(ASTAssignment node, Object data) {
        Display.Reference reference = null;


        String name = getTokenOfChild(node, 0);
        if (node.isObject) {
            reference = getReferenceFromMember((ASTMemIdentifier) node.jjtGetChild(0));
        } else {
            reference = scope.findReference(name);
            if (reference == null) {
                reference = scope.defineVariable(name);
            }
        }

        reference.setValue(doChild(node, 1));
        return data;

    }

    // OR
    public Object visit(ASTOr node, Object data) {
        return doChild(node, 0).or(doChild(node, 1));
    }

    // AND
    public Object visit(ASTAnd node, Object data) {
        return doChild(node, 0).and(doChild(node, 1));
    }

    // ==
    public Object visit(ASTCompEqual node, Object data) {
        return doChild(node, 0).eq(doChild(node, 1));
    }

    // !=
    public Object visit(ASTCompNequal node, Object data) {
        return doChild(node, 0).neq(doChild(node, 1));
    }

    // >=
    public Object visit(ASTCompGTE node, Object data) {
        return doChild(node, 0).gte(doChild(node, 1));
    }

    // <=
    public Object visit(ASTCompLTE node, Object data) {
        return doChild(node, 0).lte(doChild(node, 1));
    }

    // >
    public Object visit(ASTCompGT node, Object data) {
        return doChild(node, 0).gt(doChild(node, 1));
    }

    // <
    public Object visit(ASTCompLT node, Object data) {
        return doChild(node, 0).lt(doChild(node, 1));
    }

    // +
    public Object visit(ASTAdd node, Object data) {
        return doChild(node, 0).add(doChild(node, 1));
    }

    // -
    public Object visit(ASTSubtract node, Object data) {
        return doChild(node, 0).subtract(doChild(node, 1));
    }

    // *
    public Object visit(ASTTimes node, Object data) {
        return doChild(node, 0).mult(doChild(node, 1));
    }

    // /
    public Object visit(ASTDivide node, Object data) {
        return doChild(node, 0).div(doChild(node, 1));
    }

    // NOT
    public Object visit(ASTUnaryNot node, Object data) {
        return doChild(node, 0).not();
    }

    // + (unary)
    public Object visit(ASTUnaryPlus node, Object data) {
        return doChild(node, 0).unary_plus();
    }

    // - (unary)
    public Object visit(ASTUnaryMinus node, Object data) {
        return doChild(node, 0).unary_minus();
    }


    // Return integer literal
    public Object visit(ASTInteger node, Object data) {
        if (node.optimized == null)
            node.optimized = new ValueInteger(Integer.parseInt(node.tokenValue));
        return node.optimized;
    }

    // Return integer literal
    public Object visit(ASTRational node, Object data) {
        if (node.optimized == null)
            node.optimized = new ValueRational(Double.parseDouble(node.tokenValue));
        return node.optimized;
    }


    // Return string literal
    public Object visit(ASTCharacter node, Object data) {
        if (node.optimized == null)
            node.optimized = ValueString.stripDelimited(node.tokenValue);
        return node.optimized;
    }


    // Return true literal
    public Object visit(ASTTrue node, Object data) {
        if (node.optimized == null)
            node.optimized = new ValueBoolean(true);
        return node.optimized;
    }

    // Return false literal
    public Object visit(ASTFalse node, Object data) {
        if (node.optimized == null)
            node.optimized = new ValueBoolean(false);
        return node.optimized;
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {
        PerformInNewScope(() -> {
            while (true) {
                // evaluate loop test
                Value hopefullyValueBoolean = doChild(node, 0);
                if (!(hopefullyValueBoolean instanceof ValueBoolean))
                    throw new ExceptionSemantic("The test expression of a for loop must be boolean.");
                if (!((ValueBoolean) hopefullyValueBoolean).booleanValue())
                    break;
                // do loop statement
                doChild(node, 1, null);
            }

            return null;
        });
        return data;
    }

    @Override
    public Object visit(ASTClassDef node, Object data) {

        // When we are defining a class we might be defining it inside a
        // Class, or inside a scope. If it's inside a class we will pass it down in the data
        ClassDefinitionRepository storage = data instanceof ClassDefinitionRepository
                ? (ClassDefinitionRepository) data : scope;


        // Child 0 - identifier (fn name)
        String className = getTokenOfChild(node, 0);
        ClassDefinition classDef;

        if (node.polymorphic) {
            String subclassName = getTokenOfChild(node, 1);
            ClassDefinition subclass = scope.getClassDefinition(subclassName);

            if (subclass == null)
                throw new ExceptionSemantic("class " + className + " doesn't exists.");

            // Get the old classDefinition.
            classDef = new ClassDefinition(className, subclass);

        } else {
            classDef = new ClassDefinition(className, scope);
        }
        if (storage.classDefinitionExists(classDef)) {
            throw new ExceptionSemantic("class " + className + " already exists.");
        }

        // We want to ensure the scoping is correct here, so ensure that it knows it's
        // an object we want.
        for (int i = 1; i < node.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(i);
            child.isObject = true;
            child.jjtAccept(this, classDef);
        }

        // Add to available class definitions..
        storage.addClassDefintion(classDef);
        // Preserve this definition for future reference, and so we don't define
        // it every time this node is processed.
        return data;
    }

    @Override
    public Object visit(ASTFileLoad node, Object data) {
        String filepath = doChild(node, 0).toString();
        boolean is_append = false;
        if (node.jjtGetNumChildren() == 2) {
            is_append = doChild(node, 1).stringValue().equals("-a");
        }

        ValueFile file = null;
        try {
            file = new ValueFile(filepath, is_append);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return file;

    }


    @Override
    public Object visit(ASTNetLoad node, Object data) {
        String socket_path = doChild(node, 0).toString();
        int port = doChild(node, 1).intValue();
        int timeout = 1000;

        if (node.jjtGetNumChildren() == 3) {
            timeout = doChild(node, 2).intValue();
        }

        ValueSocket socket = null;
        try {
            socket = new ValueSocket(socket_path, port, timeout);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return socket;
    }

    public Object visit(ASTMemAssignment node, Object data) {
        ClassDefinition def = (ClassDefinition) data;
        String name = getTokenOfChild(node, 0);

        // If we have a default branch then set the member as it's value by default
        // Otherwise initialize to null.
        Value val = (Value) doChild(node, 1, data);
        def.defineMemberAndSet(name, val);

        return data;
    }

    @Override
    public Object visit(ASTMethodInvoke node, Object data) {

        String className = getTokenOfChild(node, 0);
        Reference ref = scope.findReference(className);
        if (ref == null) {
            throw new ExceptionSemantic(className + " is undefined.");
        }

        int nodesBeforeMethod = Math.max(0, node.jjtGetNumChildren() - 2);

        for (int i = 1; i < nodesBeforeMethod; i++) {
            ClassInstance instance = ref.getValue().objValue();
            String memName = getTokenOfChild(node, i);
            ref = instance.getMember(memName);

            // If the reference is the global scope we're not referring to a member anymore.
            if (ref == null || ref.getScope().getOuterScope() == null)
                throw new ExceptionSemantic("member of " + className + ", " + memName + "  is undefined.");
        }

        ClassInstance instance = ref.getValue().objValue();
        Value[] val = (Value[]) PerformInScope(instance.getScope(), scope, () -> {
            String methodName = getTokenOfChild(node, nodesBeforeMethod);
            FunctionDefinition fndef = instance.findMethod(methodName);

            if (fndef == null)
                throw new ExceptionSemantic("Method " + methodName + " is undefined.");

            FunctionInvocation newInvocation = new FunctionInvocation(fndef);

            // Child 1 - arglist
            doChild(node, 2, newInvocation);

            return instance.executeMethod(newInvocation, this);
        });

        if (val != null) {
            return val.length == 1 ? val[0] : val;
        }
        return data;
    }

    @Override
    public Object visit(ASTObjCreate node, Object data) {
        ClassInstance instance;
        String name = getTokenOfChild(node, 0);
        ClassDefinitionRepository storage = data instanceof ClassDefinitionRepository
                ? (ClassDefinitionRepository) data : scope;


        instance = storage.createClassInstance(getTokenOfChild(node, 0));
        if (instance == null) {
            throw new ExceptionSemantic("Class definition " + name + " not found in scope");
        }

        for (int i = 1; i < node.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(i);
            child.isObject = true;
            child.jjtAccept(this, instance);
        }

        Value obj = new ValueObject(instance);

        scope.defineVariable(name).setValue(obj);


        return obj;
    }

    @Override
    public Object visit(ASTMemInstantiate node, Object data) {
        Display.Reference reference;
        ClassInstance instance = (ClassInstance) data;

        String memName = getTokenOfChild(node, 0);
        reference = instance.getMember(memName);
        // If the reference is the global scope we're not referring to a member anymore.
        if (reference == null || reference.getScope().getOuterScope() == null) {
            throw new ExceptionSemantic("member " + memName + "  is undefined.");
        }

        reference.setValue(doChildMulti(node, 1, null)[0]);
        return data;
    }

    @Override
    public Object visit(ASTMethodInstantiate node, Object data) {
        FunctionDefinition reference;
        ClassInstance instance = (ClassInstance) data;

        String methodName = getTokenOfChild(node, 0);
        reference = instance.findMethod(methodName);
        // If the reference is the global scope we're not referring to a member anymore.
        if (reference == null) {
            throw new ExceptionSemantic("can not find method " + methodName);
        }

        FunctionInvocation newInvocation = new FunctionInvocation(reference);
        // Child 1 - arglist
        doChild(node, 1, newInvocation);


        Object retVal = PerformInScope(instance.getScope(), scope,() -> instance.executeMethod(newInvocation, this));

        return reference.hasReturn() ? retVal : data;
    }

    @Override
    public Object visit(ASTLabmdaDefine node, Object data) {

        FunctionDefinition currentFunctionDefinition = new FunctionDefinition("", scope.getLevel() + 1, scope);

        // Child 1 - function definition parameter list
        doChild(node, 0, currentFunctionDefinition);
        // Child 2 - function body
        currentFunctionDefinition.setFunctionBody(getChild(node, 1));

        // Child 3 - optional return expression
        if (node.fnHasReturn)
            currentFunctionDefinition.setFunctionReturnExpression(getChild(node, 2));

        Value fnObj = new ValueFunction(currentFunctionDefinition);
        return fnObj;
    }


    @Override
    public Object visit(ASTTupleDefine node, Object data) {
        int numChildren = node.jjtGetNumChildren();
        ArrayList<Value> vals = new ArrayList<>();
        for (int i = 0; i < numChildren; i++) {
            Value[] children = doChildMulti(node, i, data);

            for (Value child : children) {
                vals.add(child);
            }
        }

        return new ValueTuple(vals);
    }


    // Perform a new scope.
    private Object PerformInNewScope(ScopeRunner runner) {
        return PerformInScope(new Display(scope), runner);
    }

    // Perform In the specified scope, then return to the eold one.
    private Object PerformInScope(Display scopeRunner, ScopeRunner runner) {
        return PerformInScope(scopeRunner, null, runner);
    }

    // Perform In the specified scope, then return to the eold one.
    private Object PerformInScope(Display scopeRunner, Display calledScope, ScopeRunner runner) {
        Display oldScope = scope;
        scope = scopeRunner;
        scopeRunner.setCalledScope(calledScope);
        Object ret = runner.doAndReturn();
        scope = oldScope;
        scopeRunner.setCalledScope(null);

        return ret;
    }
}
