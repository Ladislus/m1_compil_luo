package ir.translation;

import ir.Frame;
import ir.Register;
import ir.Type;
import ir.com.Label;
import support.ListTools;

import java.util.LinkedList;

public class PredefinedFrames
{
    public static final Frame LENGTH =
            new ir.Frame(Label.named("entryLength"),
                    Label.named("exitLength"),
                    ListTools.singleton(new Register(ir.Type.ADDRESS)),
                    ListTools.singleton(false),
                    new Register(ir.Type.INT));

    public static final Frame NEW =
            new ir.Frame(Label.named("entryNew"),
                    Label.named("exitNew"),
                    ListTools.two(new Register(ir.Type.INT), new Register(ir.Type.INT)),
                    ListTools.two(false, false),
                    new Register(Type.ADDRESS)
            );

    public static final Frame INT_TO_CHAR =
            new ir.Frame(Label.named("entryIntToChar"),
                    Label.named("exitIntToChar"),
                    ListTools.singleton(new Register(Type.INT)),
                    ListTools.singleton(false),
                    new Register(Type.BYTE));

    public static final Frame CHAR_TO_INT =
            new ir.Frame(Label.named("entryCharToInt"),
                    Label.named("exitCharToInt"),
                    ListTools.singleton(new Register(Type.BYTE)),
                    ListTools.singleton(false),
                    new Register(Type.INT));

    public static final Frame INPUT_INT =
            new ir.Frame(Label.named("entryReadInt"),
                    Label.named("exitReadInt"),
                    new LinkedList<>(),
                    new LinkedList<>(),
                    new Register(Type.INT));

    public static final Frame INPUT_CHAR =
            new ir.Frame(Label.named("entryReadChar"),
                    Label.named("exitReadChar"),
                    new LinkedList<>(),
                    new LinkedList<>(),
                    new Register(Type.BYTE));

    public static final Frame INPUT_STRING =
            new ir.Frame(Label.named("entryReadString"),
                    Label.named("exitReadString"),
                    new LinkedList<>(),
                    new LinkedList<>(),
                    new Register(Type.ADDRESS));

    public static final Frame INPUT_BOOL =
            new ir.Frame(Label.named("entryReadBool"),
                    Label.named("exitReadBool"),
                    new LinkedList<>(),
                    new LinkedList<>(),
                    new Register(Type.BYTE));

    public static final Frame PRINT_INT =
            new ir.Frame(Label.named("entryPrintInt"),
                Label.named("exitPrintInt"),
                ListTools.singleton(new Register(ir.Type.INT)),
                ListTools.singleton(false));

    public static final Frame PRINT_CHAR =
            new ir.Frame(Label.named("entryPrintChar"),
                    Label.named("exitPrintChar"),
                    ListTools.singleton(new Register(ir.Type.BYTE)),
                    ListTools.singleton(false));

    public static final Frame PRINT_BOOL =
            new ir.Frame(Label.named("entryPrintBool"),
                    Label.named("exitPrintBool"),
                    ListTools.singleton(new Register(ir.Type.BYTE)),
                    ListTools.singleton(false));

    public static final Frame PRINT_STRING =
            new ir.Frame(Label.named("entryPrintString"),
                    Label.named("exitPrintString"),
                    ListTools.singleton(new Register(Type.ADDRESS)),
                    ListTools.singleton(false));
}
