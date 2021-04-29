package luo;

import ast.Build;
import ir.Frame;
import ir.com.Command;
import ir.com.Label;
import mips.Program;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import parser.LuoLexer;
import parser.LuoParser;
import ir.translation.Translate;
import semantic_analysis.SymbolTable;
import semantic_analysis.SymbolTableBuilder;
import support.Errors;
import support.Pair;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;

public class Main {

    private enum ErrorCode {
        SUCCESS,
        NO_FILE_NAME,
        FILE_NOT_FOUND,
        SYNTAX_ERROR,
        SEMANTIC_ERROR,
        TRANSLATION_TO_IR_ERROR,
        COMPILATION_ERROR
    }

    private static void exitWithCode(ErrorCode code){
        System.exit(code.ordinal());
    }

    private static void ifHasErrorsPrintAndExit(Errors errors, ErrorCode code){
        if (errors.hasErrors()){
            errors.print();
            exitWithCode(code);
        }
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
            exitWithCode(ErrorCode.FILE_NOT_FOUND);
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
            exitWithCode(ErrorCode.SYNTAX_ERROR);
        return tree;
    }

    private static SymbolTable analyze(ast.Program program){
        SymbolTableBuilder builder = new SymbolTableBuilder();
        program.accept(builder);
        ifHasErrorsPrintAndExit(builder.getErrors(), ErrorCode.SEMANTIC_ERROR);
        return builder.getSymbolTable();
    }

    private static Pair<Label, List<Pair<Frame, List<Command>>>> translate(SymbolTable symbolTable, ast.Program program){
        Pair<Label, List<Pair<Frame, List<Command>>>> result = Translate.run(symbolTable, program);
        ifHasErrorsPrintAndExit(Translate.errors, ErrorCode.TRANSLATION_TO_IR_ERROR);
        return result;
    }

    private static Path changeExtension(Path path, String oldExt, String newExt) {
        PathMatcher pm = FileSystems.getDefault()
                .getPathMatcher("glob:*" + oldExt);
        if (pm.matches(path.getFileName())) {
            String nameWithExtension = path.getFileName().toString();
            int endIndex = nameWithExtension.length() - oldExt.length();
            String name = nameWithExtension.substring(0, endIndex);
            if (path.getParent() != null)
                return path.getParent().resolve(name + newExt);
            else
                return FileSystems.getDefault().getPath(name + newExt);
        }
        return path;
    }

    private static void compile(Path path,
                                Label mainLabel,
                                List<Pair<Frame, List<Command>>> fragments) {
        Path newPath = FileSystems.getDefault().getPath(
                changeExtension(path, ".luo", ".asm").getFileName().toString()
        );
        mips.Program.generate(newPath, mainLabel, fragments);
        ifHasErrorsPrintAndExit(mips.Program.errors, ErrorCode.COMPILATION_ERROR);
    }

    private static void irTranslation(ast.Program program, SymbolTable symbolTable) {
        Translate.run(symbolTable, program);
    }

    public static void main(String[] arguments) throws IOException {
        if (arguments.length == 0)
            // No name given to the command line
            exitWithCode(ErrorCode.NO_FILE_NAME);
        String fileName = arguments[0];
        InputStream inputStream = getInputStream(fileName);
        ParseTree parseTree = parse(inputStream);
        ast.Program program = buildAst(parseTree);
        SymbolTable symbolTable = analyze(program);
        Pair<Label, List<Pair<Frame, List<Command>>>> irRepresentation = translate(symbolTable, program);
        Path path = FileSystems.getDefault().getPath(fileName);
        compile(path, irRepresentation.getFst(), irRepresentation.getSnd());
        exitWithCode(ErrorCode.SUCCESS);
    }
}
