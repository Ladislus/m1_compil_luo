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
   (imports)*
   (global_declaration|type_definition|function_definition)*
    EOF
   ;


global_declaration:
       visibilite? Static declaration
       ;

declaration:
          type_expression Identifier EqualSymbol expression      #LocalDeclarationInit
       |  type_expression Identifier                             #LocalDeclaration
       ;

instruction :
     // Tous les mots-clés sont à définir en tant que lexèmes, et pour tous
        // Il manque : l'affectation, return, break
            If OpenedParenthesis expression ClosedParenthesis instruction
            (Elseif OpenedParenthesis expression ClosedParenthesis instruction)*
            (Else OpenedParenthesis expression ClosedParenthesis instruction)?                                          #If
        |   Foreach OpenedParenthesis type_expression Identifier Colon expression ClosedParenthesis instruction         #Foreach
        |   For OpenedParenthesis declaration Semicolon expression Semicolon expression ClosedParenthesis instruction   #For
        |   While OpenedParenthesis expression ClosedParenthesis instruction                                            #While
        |   Do instruction While OpenedParenthesis expression ClosedParenthesis                                         #Dowhile
        |   expression                                                                                                  #InsExpression
        |   OpenBracket (declaration)* (instruction)* ClosedBracket                                                     #Block
        |   Return expression                                                                                           #Return
        |   Break                                                                                                       #Break
        |   expression op=(MinusEqual|PlusEqual|MultEqual|DivEqual|EqualSymbol) expression                              #Affectation
        ;
expression :
      Identifier OpenedParenthesis actual_parameter_list? ClosedParenthesis                                                                 #FunctionCall
    | expression OpenSquareBracket expression ClosedSquareBracket                                                                           #AccessTabDico
    | expression Dot Identifier                                                                                                             #AccessRec
    | OpenedParenthesis expression ClosedParenthesis                                                                                        #Parenthesis
    | (PlusPlus | MinusMinus) expression                                                                                                    #PreUnary
    | expression (PlusPlus | MinusMinus)                                                                                                    #PostUnary
    | expression op=(Multiplication | Division | Modulo) expression                                                                         #MulDivMod
    | expression op=(Plus | Minus) expression                                                                                               #AddSub
    | expression op=(GreaterThan | GreaterOrEqual | LesserThan | LesserOrEqual | Different | Equal ) expression                             #Comparison
    | expression LogicalAnd expression                                                                                                      #And
    | expression LogicalOr expression                                                                                                       #Or
    | Negation expression                                                                                                                   #Not
    | Minus expression                                                                                                                      #Opposite
    | Integer                                                                                                                               #Integer
    | Character                                                                                                                             #Character
    | String                                                                                                                                #String
    | Boolean                                                                                                                               #Boolean
    | Identifier                                                                                                                            #Identifier
    ;

actual_parameter_list:
      ((expression Comma)* expression)?
      ;

type_definition : Rec type_expression OpenBracket (type_expression Identifier Semicolon)* ClosedBracket Semicolon;

type_expression:
    (IntegerType|BooleanType|CharType)
    |type_expression Array
    |Identifier Map
    |Identifier
    ;

function_definition : visibilite type_expression Identifier OpenedParenthesis argument_list? ClosedParenthesis OpenBracket (instruction*)? Return expression ClosedBracket
    | visibilite Void Identifier OpenedParenthesis argument_list? ClosedParenthesis OpenBracket (instruction*)? Return expression ClosedBracket;

visibilite : Public
    |   Private;

argument_list : (Identifier type_expression (Colon expression)?)
    | ((Identifier type_expression (Colon expression)?) Comma)* (Identifier type_expression (Colon expression)?);

imports :  Import DoubleQuote (repertoire*)? Identifier DoubleQuote;

repertoire : Root
    |   Identifier Division
    |   Parent
    |   Division;

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
Void:'void';
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
Integer: (Minus)?Digit+;
Digit:  [0-9];
Semicolon: ';';
Map: 'map';
Rec: 'rec';
Comma: ',';
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
Root:'./';
Parent:'../';

//*******************************************************
//* Don't write anything below these 3 lines
Identifier: (Underscore|Letter)(Underscore|Letter|Digit)*;
Letter: [a-zA-Z];
WS: [ \t\r\n]+ -> skip;




