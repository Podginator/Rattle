package interpreter;

/**
 * This is the base class for every AST node.
 *
 * @Author Tom
 * @author Dave.
 */


public class BaseASTNode {


    // The actual source code from which the token was constructed.  Only set on literals, etc.
    public String tokenValue = null;

    /**
     * Set at parse-time in an IF ... ELSE construct to indicate to the compiler
     * or interpreter whether or not an IF clause has an ELSE.
     */
    public boolean ifHasElse = false;

    /**
     * Set at parse-time in a function definition to indicate whether or not the function
     * has a return value.
     */
    public boolean fnHasReturn = false;

    /**
     * Does a member have a default value?
     */
    public boolean memHasDefaultVal = false;


    public boolean polymorphic = false;

    /**
     * Is what we're referencing an object
     */
    public boolean isObject = false;
}
