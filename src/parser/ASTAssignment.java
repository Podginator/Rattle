/* Generated By:JJTree: Do not edit this line. ASTAssignment.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=interpreter.BaseASTNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTAssignment extends SimpleNode {
  public ASTAssignment(int id) {
    super(id);
  }

  public ASTAssignment(Rattle p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(RattleVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ca196a1f30766de71879c9d77e45169e (do not edit this line) */
