# Listes des tests effectués

 **Fonctions :**
 - Vérification si la fonction est déjà définie
    - Ou : TableBuilder.visit(Function)
    - Vérificateur : SymbolTable.insertFunction(String, Signature)
 - Vérification si le nom de fonction existe lors d'un appel de fonction (aucune vérification sur la signature)
    - Ou : TableBuilder.visit(Program)
    - Vérificateur : SymbolTable.funcLookup(String)
    

**Variables :**
 - Vérification que la variable locale n'est pas déjà définie dans le block courant
    - Ou : TableBuilder.visit(Declaration)
    - Vérificateur : SymbolTable.insertVariable(String, InsBlock, Type)
 - Vérification que la variable globale n'est pas déjà définie
    - Ou : TableBuilder.visit(GlobalDeclaration)
    - Vérificateur : SymbolTable.insertGlobalVariable(String, Type)
 - Vérification qu'une variable utilisée est définie
   - Ou : TableBuilder.visit(ExpVariable)
   - Vérificateur : SymbolTable.varLookup(String, VisitedBlocks)
    

**User types :**
- Vérification qu'un UserType n'est pas déjà défini
     - Ou : TableBuilder.visit(TypeDefinition)
     - Vérificateur : SymbolTable.InsertUserType(TypeDefinition)
    
