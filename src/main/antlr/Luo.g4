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
 //  (imports)*
 //  (global_declaration|type_definition|function_definition)*
   (instruction ';')*
    EOF
   ;

global_declaration:
       (Public|Private)? Static declaration
       ;

declaration:
          type_expression Identifier EqualSymbol expression      #LocalDeclarationInit
       |  type_expression Identifier                             #LocalDeclaration
       ;

instruction :
    // Tous les mots-clés sont à définir en tant que lexèmes, et pour tous
    // les symboles il faut utiliser les lexèmes déjà définis, c'est fait pour #Block
    // à titre d'exemple. À faire partout.
    // Il manque : l'affectation, return, break
        'if' '(' expression ')' instruction
        ('elseif' '(' expression ')' instruction)*
        ('else' '(' expression ')' instruction)?                                        #If
    |   'foreach' '(' type_expression Identifier ':' expression ')' instruction         #Foreach
    |   'for' '(' declaration ';' expression ';' expression ')' instruction             #For
    |   'while' '(' expression ')' instruction                                          #While
    |   'do' instruction 'while' '(' expression ')'                                     #Dowhile
    |   expression                                                                      #InsExpression
    |   OpenBracket (declaration)* (instruction)* ClosedBracket                         #Block
    ;

expression :
      Identifier OpenedParenthesis actual_parameter_list? ClosedParenthesis                                                                 #FunctionCall
    | expression OpenSquareBracket expression ClosedSquareBracket                                                                           #AccessTabDico
    | expression Dot Identifier                                                                                                             #AccessRec
    | OpenedParenthesis expression ClosedParenthesis                                                                                        #Parenthesis
    | (PlusPlus | MinusMinus) expression                                                                                                    #Increment
    | expression (PlusPlus | MinusMinus)                                                                                                    #Decrement
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
      ((expression Comma)* expression)*
      ;

type_definition : Rec type_expression OpenBracket (type_expression Identifier Semicolon)* ClosedBracket;

type_expression:
// À revoir complètement. Les expressions de types sont :
// 1. les types prédéfinis: int, bool, char
// 2. les types tableaux, par exemple int array, char array, (char array) array
// 3. les types dictonnaires
// 4. les noms de types définis par l'utilisateur (donc des identifiants).
//
// L'utilisation basique des expressions de types : donner un type à une variable dans:
// - les déclarations locales, exemples: int x, mon_type tmp
// - les déclarations globales, exemple: public static int array Stack
// - les paramètres de fonctions, exemple void f(int array t)
// - les déclarations des boucles for (initialisation possible) et des boucles foreach (pas d'initialisation possible)
//
// Attention on ne peut pas utiliser '*' pour les types dictionnaires, car nous avons décidé de NE PAS avoir les types tuples.
// Du coup, ça pose un problème pour l'instruction foreach, car elle doit être de la forme:
// foreach(int x : t) où int array t
// Dans le cas où t est un type dictionnaire, par exemple (char * int) dico si on prend
// la proposition de syntaxe (qui n'est pas bonne), le type de x serait char * int.
// Mais on n'a pas les types tuples, donc pas char * int.
// Comment faire ?
// Utiliser map plutôt que dico.
    Rec Identifier OpenBracket (declaration)*  ClosedBracket Semicolon // Qu'est ce que ça représente comme type ?
    | type_definition OpenSquareBracket ClosedSquareBracket Identifier EqualSymbol IntList Semicolon // Idem ?
    | type_definition OpenSquareBracket  Integer ClosedSquareBracket Identifier Semicolon // Pas de nombres dans les types !
    | type_definition Multiplication type_definition Dico Identifier Semicolon // pas de ; dans les types
    ;
// Ceci est une définition de type, pas une expression de type
//    | type_definition'*'type_definition Dico Identifier EqualSymbol OpenBracket (OpenedParenthesis IdentifierStr Comma
//    Identifier ClosedParenthesis)*
//    (Comma OpenedParenthesis IdentifierStr Comma Identifier ClosedParenthesis )* ClosedBracket Semicolon

function_definition : visibilite type_expression Identifier OpenedParenthesis argument_list? ClosedParenthesis OpenBracket (instruction*)? Return expression ClosedBracket
    | visibilite Void Identifier OpenedParenthesis argument_list? ClosedParenthesis OpenBracket (instruction*)? Return expression ClosedBracket;

visibilite : Public
    |   Private;

argument_list : (Identifier type_expression (Colon expression)?)
    | ((Identifier type_expression (Colon expression)?) Comma)* (Identifier type_expression (Colon expression)?);

imports : Import DoubleQuote ((Root|Division|'../')? Identifier (Division)? ) DoubleQuote
    | Import DoubleQuote ((Root|Division|'../')? Identifier (Division)? ) ((Division|'../')? Identifier (Division)? ) DoubleQuote;


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
Dico: 'dico';
Rec: 'rec';
Comma: ',';
Dot: '.';
OpenBracket: '{';
ClosedBracket: '}';
OpenSquareBracket: '[';
ClosedSquareBracket: ']';
EqualSymbol: '=';
Import:'import';
Return:'return';
Colon:':';
Root:'./';
Parent:'../';
Identifier: (Underscore|Letter)(Underscore|Letter|Digit)*;
Letter: [a-zA-Z];
WS: [ \t\r\n]+ -> skip;