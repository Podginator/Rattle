/* Generated By:JJTree: Do not edit this line. ASTUnaryMinus.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=interpreter.BaseASTNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTUnaryMinus extends SimpleNode {
  public ASTUnaryMinus(int id) {
    super(id);
  }

  public ASTUnaryMinus(Rattle p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(RattleVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5c551b69dde02121236353832ebfa13f (do not edit this line) */
