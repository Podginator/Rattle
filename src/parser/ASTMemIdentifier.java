/* Generated By:JJTree: Do not edit this line. ASTMemIdentifier.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=interpreter.BaseASTNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTMemIdentifier extends SimpleNode {
  public ASTMemIdentifier(int id) {
    super(id);
  }

  public ASTMemIdentifier(Rattle p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(RattleVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=6ccc832bb22f2d94557dcc89f3be9402 (do not edit this line) */
