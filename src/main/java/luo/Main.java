package luo;

import ast.Build;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import parser.LuoLexer;
import parser.LuoParser;
import ir.translation.Translate;

import java.io.*;

public class Main {

    private enum Error {
        SUCCESS,
        NO_FILE_NAME,
        FILE_NOT_FOUND,
        SYNTAX_ERROR
    }

    private static ast.Program buildAst(ParseTree parseTree){
        ast.Build builder = new Build();
        return (ast.Program) parseTree.accept(builder);
    }

    private static InputStream getInputStream(String fileName){
        try {
            if (fileName != null)
                return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.exit(Error.FILE_NOT_FOUND.ordinal());
        }
        return System.in;
    }

    private static ParseTree parse(InputStream inputStream) throws IOException {
        CharStream input = CharStreams.fromStream(inputStream);
        // Creation of the lexer for pico programs
        LuoLexer lexer = new LuoLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // Creation of the parser for pico programs
        LuoParser parser = new LuoParser(tokens);
        // Parse the input: the result is a parse tree
        ParseTree tree = parser.program();
        if (parser.getNumberOfSyntaxErrors() != 0)
            System.exit(Error.SYNTAX_ERROR.ordinal());
        return tree;
    }

    private static void compile(ast.Program program){
        // TO COMPLETE
    }

    public static void main(String[] arguments) throws IOException {
        if (arguments.length == 0)
            // No name given to the command line
            System.exit(Error.NO_FILE_NAME.ordinal());
        String fileName = arguments[0];
        InputStream inputStream = getInputStream(fileName);
        ParseTree parseTree = parse(inputStream);
        ast.Program program = buildAst(parseTree);
        printer.NotSoPretty.print(program);
        // All is fine
        System.exit(Error.SUCCESS.ordinal());
    }
}
