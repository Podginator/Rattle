/* Generated By:JJTree: Do not edit this line. ASTSubtract.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=interpreter.BaseASTNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTSubtract extends SimpleNode {
  public ASTSubtract(int id) {
    super(id);
  }

  public ASTSubtract(Rattle p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(RattleVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=36b3e6c0326b53fe8e8f892a8698a9b0 (do not edit this line) */
