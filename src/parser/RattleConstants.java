/* Generated By:JJTree&JavaCC: Do not edit this line. RattleConstants.java */
package parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface RattleConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 6;
  /** RegularExpression Id. */
  int FORMAL_COMMENT = 7;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT = 8;
  /** RegularExpression Id. */
  int IF = 9;
  /** RegularExpression Id. */
  int ELSE = 10;
  /** RegularExpression Id. */
  int FOR = 11;
  /** RegularExpression Id. */
  int WHILE = 12;
  /** RegularExpression Id. */
  int PRINT = 13;
  /** RegularExpression Id. */
  int OR = 14;
  /** RegularExpression Id. */
  int AND = 15;
  /** RegularExpression Id. */
  int NOT = 16;
  /** RegularExpression Id. */
  int TRUE = 17;
  /** RegularExpression Id. */
  int FALSE = 18;
  /** RegularExpression Id. */
  int NULL = 19;
  /** RegularExpression Id. */
  int FN = 20;
  /** RegularExpression Id. */
  int CLASS = 21;
  /** RegularExpression Id. */
  int RETURN = 22;
  /** RegularExpression Id. */
  int QUIT = 23;
  /** RegularExpression Id. */
  int CREATE = 24;
  /** RegularExpression Id. */
  int INTEGER_LITERAL = 25;
  /** RegularExpression Id. */
  int DECIMAL_LITERAL = 26;
  /** RegularExpression Id. */
  int HEX_LITERAL = 27;
  /** RegularExpression Id. */
  int OCTAL_LITERAL = 28;
  /** RegularExpression Id. */
  int FLOATING_POINT_LITERAL = 29;
  /** RegularExpression Id. */
  int EXPONENT = 30;
  /** RegularExpression Id. */
  int STRING_LITERAL = 31;
  /** RegularExpression Id. */
  int IDENTIFIER = 32;
  /** RegularExpression Id. */
  int LETTER = 33;
  /** RegularExpression Id. */
  int DIGIT = 34;
  /** RegularExpression Id. */
  int LPAREN = 35;
  /** RegularExpression Id. */
  int RPAREN = 36;
  /** RegularExpression Id. */
  int LBRACE = 37;
  /** RegularExpression Id. */
  int RBRACE = 38;
  /** RegularExpression Id. */
  int LBRACKET = 39;
  /** RegularExpression Id. */
  int RBRACKET = 40;
  /** RegularExpression Id. */
  int SEMICOLON = 41;
  /** RegularExpression Id. */
  int COMMA = 42;
  /** RegularExpression Id. */
  int DOT = 43;
  /** RegularExpression Id. */
  int PP = 44;
  /** RegularExpression Id. */
  int MM = 45;
  /** RegularExpression Id. */
  int ASSIGN = 46;
  /** RegularExpression Id. */
  int PEQ = 47;
  /** RegularExpression Id. */
  int MEQ = 48;
  /** RegularExpression Id. */
  int UEQ = 49;
  /** RegularExpression Id. */
  int EQ = 50;
  /** RegularExpression Id. */
  int OBJDEREF = 51;
  /** RegularExpression Id. */
  int GT = 52;
  /** RegularExpression Id. */
  int LT = 53;
  /** RegularExpression Id. */
  int LE = 54;
  /** RegularExpression Id. */
  int GE = 55;
  /** RegularExpression Id. */
  int NE = 56;
  /** RegularExpression Id. */
  int HOOK = 57;
  /** RegularExpression Id. */
  int COLON = 58;
  /** RegularExpression Id. */
  int PLUS = 59;
  /** RegularExpression Id. */
  int SUBT = 60;
  /** RegularExpression Id. */
  int STAR = 61;
  /** RegularExpression Id. */
  int SLASH = 62;
  /** RegularExpression Id. */
  int REM = 63;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "<SINGLE_LINE_COMMENT>",
    "<FORMAL_COMMENT>",
    "<MULTI_LINE_COMMENT>",
    "\"IF\"",
    "\"ELSE\"",
    "\"FOR\"",
    "\"WHILE\"",
    "\"WRITE\"",
    "\"OR\"",
    "\"AND\"",
    "\"NOT\"",
    "\"TRUE\"",
    "\"FALSE\"",
    "\"NONE\"",
    "\"DEF\"",
    "\"CLASS\"",
    "\"RETURN\"",
    "\"QUIT\"",
    "\"CREATE\"",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEX_LITERAL>",
    "<OCTAL_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<STRING_LITERAL>",
    "<IDENTIFIER>",
    "<LETTER>",
    "<DIGIT>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"++\"",
    "\"--\"",
    "\"=\"",
    "\"+=\"",
    "\"-=\"",
    "\"@=\"",
    "\"==\"",
    "\"OBJ\"",
    "\">\"",
    "\"<\"",
    "\"<=\"",
    "\">=\"",
    "\"!=\"",
    "\"?\"",
    "\":\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"%\"",
    "\"extends \"",
    "\"net::(\"",
    "\"file::(\"",
    "\" where\"",
    "\"->\"",
  };

}
