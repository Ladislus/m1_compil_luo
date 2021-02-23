grammar Luo;

// The non-terminal (for grammar rules) will include:
// - programs: for LUO programs
// - function_definition: for function definitions
// - global_declaration: for global variable declarations
// - import: for import directives
// - type_definition: for definitions of types (basically records)
// - type_expression: for type expressions like int, bool, char array,
//                    or the type of a dictionary (what is it?)
// - declaration: for declarations
// - instruction: for instructions, including blocks
// - expression: for expressions
// DO NOT introduce a new non-terminal without discussing it with the whole class.

// TO MODIFY:
program: EOF;

expression : expression op=(Multiplication | Division | Modulo) expression                                                                  #MulDivMod
    | expression op=(Plus | Minus) expression                                                                                               #AddSub
    | expression op=(GreaterThan | GreaterOrEqual | LesserThan | LesserOrEqual | Different | Equal | LogicalAnd | LogicalOr) expression     #Comparison
    | Negation expression                                                                                                                   #Not
    | Minus expression                                                                                                                      #Opposite
    | OpenedParenthesis expression ClosedParenthesis                                                                                        #Parenthesis
    | Integer                                                                                                                               #Integer
    | Character                                                                                                                             #Character
    | String                                                                                                                                #String
    | Boolean                                                                                                                               #Boolean
    | Identifier                                                                                                                            #Identifier
    ;
// Some lexer rules.
// Additional rules are needed for all the keywords and reserved symbols.
// The naming convention for these new rules is that the non-terminal
//   is either the LUO keywork if it is a letter only keyword, or the
//   english word to denote the symbol (see the Minus and Underscore
//   examples below).
// Additional rules are needed for character and string constants.
// Additional rules are needed for comments .
Static: 'static';
Public: 'public';
Private: 'private';
Minus: '-';
Plus: '+';
Multiplication: '*';
Division: '/';
Modulo: '%';
GreaterThan: '>';
GreaterOrEqual: '>=';
Equal: '==';
LesserThan: '<';
LesserOrEqual: '<=';
Different: '!=';
Negation: '!';
OpenedParenthesis: '(';
ClosedParenthesis: ')';
LogicalOr: '||';
LogicalAnd: '&&';
Underscore : '_';
Letter: [a-zA-Z];
SimpleQuote: '\'';
DoubleQuote: '"';
Boolean: True | False;
True: 'true';
False: 'false';
Escape: '\\';
NewLine: Escape 'n';
Tabulation: Escape 't';
Character: SimpleQuote (NewLine | Tabulation | ~[']) SimpleQuote;
String: DoubleQuote ~["]* DoubleQuote;
Integer: (Minus)?Digit+;
Digit:  [0-9];
Semicolon: ';';
Identifier: (Underscore|Letter)(Underscore|Letter|Digit)*;
WS: [ \t\r\n]+ -> skip;