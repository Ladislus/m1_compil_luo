package luo;

import org.antlr.v4.runtime.tree.ParseTree;

import java.io.InputStream;

public class Main {

    private static ast.Program buildAst(ParseTree parseTree){
        // TO COMPLETE
        return null;
    }

    private static InputStream getInputStream(String fileName){
        // TO COMPLETE
        return null;
    }

    private static ParseTree parse(InputStream inputStream){
        // TO COMPLETE
        return null;
    }

    private static void compile(ast.Program program){
        // TO COMPLETE
    }

    public static void main(String[] arguments) {
        if (arguments.length == 0)
            // No name given to the command line
            System.exit(1);
        String fileName = arguments[0];
        InputStream inputStream = getInputStream(fileName);
        ParseTree parseTree = parse(inputStream);
        ast.Program program = buildAst(parseTree);
        compile(program);
        // All is fine
        System.out.println("Done");
        System.exit(0);
    }
}
