package interpreter;

import parser.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Interpreter {

    private static void usage() {
        System.out.println("Usage: rattle [-d1] < <source>");
        System.out.println("          -d1 -- output AST");
    }

    public static void main(String args[]) {
        boolean debugAST = false;
        if (args.length == 1) {
            if (args[0].equals("-d1"))
                debugAST = true;
            else {
                usage();
                //return;
            }
        }
        if (args.length == 2) {
            try {
                System.setIn(new FileInputStream(args[1]));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Rattle language = new Rattle(System.in);
        try {
            ASTCode parser = language.code();
            RattleVisitor nodeVisitor;

            if (debugAST)
                nodeVisitor = new Parser();
            else
                nodeVisitor = new Parser();
            parser.jjtAccept(nodeVisitor, null);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }
}
