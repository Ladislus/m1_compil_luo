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
program:
   (imports Semicolon?)*
   (type_definition|function_definition|global_declaration)*
    EOF
   ;

function_definition :
    Visibility? type_expression Identifier OpenedParenthesis argument_list? ClosedParenthesis block  #DefinitionFunction
    ;


global_declaration: Visibility? Static declaration;

declaration:
          type_expression Identifier EqualSymbol expression Semicolon?     #LocalDeclarationInit
       |  type_expression Identifier                        Semicolon?     #LocalDeclaration
       ;

block : OpenBracket (declaration)* (instruction)* ClosedBracket ;

instruction :
            If OpenedParenthesis expression ClosedParenthesis instruction
            (Elseif OpenedParenthesis expression ClosedParenthesis instruction)*
            (Else instruction)?                                                                                         #InsIf
        |   Foreach OpenedParenthesis type_expression Identifier Colon expression ClosedParenthesis instruction         #InsForeach
        |   For OpenedParenthesis declaration Semicolon expression Semicolon expression ClosedParenthesis instruction   #InsFor
        |   While OpenedParenthesis expression ClosedParenthesis instruction                                            #InsWhile
        |   Do instruction While OpenedParenthesis expression ClosedParenthesis                                         #InsDowhile
        |   expression Semicolon?                                                                                       #InsExpression
        |   block                                                                                                       #InsBlock
        |   Return expression Semicolon?                                                                                #InsReturn
        |   Break Semicolon?                                                                                            #InsBreak
        |   expression op=(MinusEqual|PlusEqual|MultEqual|DivEqual|EqualSymbol) expression Semicolon?                   #InsAssign
        ;
expression :
      Identifier OpenedParenthesis expression_list? ClosedParenthesis                                                   #ExpFunctionCall
    | OpenBracket labeled_expression_list ClosedBracket                                                                 #ExpRecordEnumeration
    | OpenBracket expression_list? ClosedBracket                                                                        #ExpArrayEnumeration
    | expression OpenSquareBracket expression ClosedSquareBracket                                                       #ExpAccessTabDico
    | expression Dot Identifier                                                                                         #ExpAccessRec
    | OpenedParenthesis expression ClosedParenthesis                                                                    #ExpParenthesis
    | op=(PlusPlus | MinusMinus) expression                                                                             #ExpPreUnary
    | expression op=(PlusPlus | MinusMinus)                                                                             #ExpPostUnary
    | expression op=(Multiplication | Division | Modulo) expression                                                     #ExpMulDivMod
    | Minus expression                                                                                                  #ExpOpposite
    | expression op=(Plus | Minus) expression                                                                           #ExpAddSub
    | expression op=(GreaterThan | GreaterOrEqual | LesserThan | LesserOrEqual | Different | Equal ) expression         #ExpComparison
    | expression op=LogicalAnd expression                                                                               #ExpAnd
    | expression op=LogicalOr expression                                                                                #ExpOr
    | Negation expression                                                                                               #ExpNot
    | Integer                                                                                                           #ExpInteger
    | Character                                                                                                         #ExpCharacter
    | String                                                                                                            #ExpString
    | Boolean                                                                                                           #ExpBoolean
    | Identifier                                                                                                        #ExpIdentifier
    | New type_expression OpenedParenthesis expression ClosedParenthesis                                          #ExpNew
    ;

expression_list:
      ((expression Comma)* expression)
      ;
labeled_expression_list :
    (Identifier EqualSymbol expression Comma)* (Identifier EqualSymbol expression)
    ;

type_definition : Rec Identifier OpenBracket (type_expression Identifier Semicolon?)+ ClosedBracket Semicolon?;

type_expression:
      type=(IntegerType|BooleanType|CharType)    #TypPrimitive
    | type_expression Array                      #TypArray
    | Identifier Map                             #TypMap
    | Identifier                                 #TypIdentifier
    ;

Visibility : Public | Private;

argument : type_expression Identifier (Colon expression)? ;

argument_list : (argument Comma)* argument;

imports : Import String;

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
PlusEqual : '+=';
MinusEqual : '-=';
MultEqual : '*=';
DivEqual : '/=';
Minus: '-';
Plus: '+';
PlusPlus: '++';
MinusMinus: '--';
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
Integer: Digit+;
Digit:  [0-9];
Semicolon: ';';
Map: 'map';
Rec: 'rec';
Comma: ',';
New: 'new';
Dot: '.';
OpenBracket: '{';
ClosedBracket: '}';
OpenSquareBracket: '[';
ClosedSquareBracket: ']';
EqualSymbol: '=';
If : 'if';
While : 'while';
Foreach : 'foreach';
Do : 'do';
Else : 'else';
Elseif : 'elseif';
For : 'for';
Break : 'break';
IntegerType: 'int';
BooleanType: 'bool';
CharType: 'char';
Array: 'array';
Import:'import';
Return:'return';
Colon:':';
Current:'./';
Parent:'../';

//*******************************************************
//* Don't write anything below these 3 lines
Identifier: (Underscore|Letter)(Underscore|Letter|Digit)*;
Letter: [a-zA-Z];
WS: [ \t\r\n]+ -> skip;