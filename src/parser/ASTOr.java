/* Generated By:JJTree: Do not edit this line. ASTOr.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=interpreter.BaseASTNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTOr extends SimpleNode {
  public ASTOr(int id) {
    super(id);
  }

  public ASTOr(Rattle p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(RattleVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7b2adc73fd6370a3f876cb5ba31a9105 (do not edit this line) */
