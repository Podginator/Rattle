/* Generated By:JJTree: Do not edit this line. ASTUnaryNot.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=interpreter.BaseASTNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTUnaryNot extends SimpleNode {
  public ASTUnaryNot(int id) {
    super(id);
  }

  public ASTUnaryNot(Rattle p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(RattleVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=fec3c350d1f0005856265aef242d302c (do not edit this line) */
