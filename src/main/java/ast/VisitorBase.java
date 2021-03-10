package ast;


import java.util.List;

public class VisitorBase<T> implements Visitor<T> {


    // ##########################
    // #    Block Expression    #
    // ##########################
    /**
     * @author Ladislas WALCAK
     * @author Thomas QUETIER
     * @author Corentin HERVOCHON
     * @author Martin GUERRAUD
     */
    @Override
    public T visit(ExpPreUnaryOperation operation) {
        return operation.getExpression().accept(this);
    }

    @Override
    public T visit(ExpPostUnaryOperation operation) {
        return operation.getExpression().accept(this);
    }

    @Override
    public T visit(ExpBinaryOperation operation) {
        operation.getLeft().accept(this);
        return operation.getRight().accept(this);
    }

    @Override
    public T visit(ExpBoolean bool) {
        return null;
    }

    @Override
    public T visit(ExpCharacter character) {
        return null;
    }

    @Override
    public T visit(ExpInteger integer) {
        return null;
    }

    @Override
    public T visit(ExpVariable variable) {
        return null;
    }

    @Override
    public T visit(ExpRecordAccess record) {
        return record.getRecord().accept(this);
    }

    @Override
    public T visit(ExpArrayAccess array) {
        array.getArray().accept(this);
        return array.getIndex().accept(this);
    }

    @Override
    public T visit(ExpEnum enumeration) {
        T curr = null;
        for (Expression exp : enumeration.getElements()) curr = exp.accept(this);
        return curr;
    }

    @Override
    public T visit(ExpTuple tuple) {
        tuple.getFirst().accept(this);
        return tuple.getSecond().accept(this);
    }

    @Override
    public T visit(ExpFunctionCall function) {
        T curr = null;
        for (Expression exp : function.getArgs()) curr = exp.accept(this);
        return curr;
    }

    @Override
    public T visit(ExpString string) {
        return null;
    }
    // ##############################
    // #    Fin Block Expression    #
    // ##############################


    /**
     *Expression de types
     * @author GUINDO Mouctar Ousseini
     * @author GBOHO Thierry
     * @author HEBRAS Jerome
     * @author GUINGOUAIN Nicolas
     *
     *
     */

    @Override
    public T visit(TypPrimitive typPrimitive) {
        return  null;
    }

    @Override
    public T visit(TypDico typDictionary) {
        return typDictionary.getType().accept(this);
    }

    @Override
    public T visit(TypVariable typRecord) {
        return null;
    }

    @Override
    public T visit(TypArray typArray) {
        return typArray.getType().accept(this);
    }

    // #################################################################################################
    // # Bloc Definition de fonction, définition de type, imports, déclarations globales et programmes #
    // #################################################################################################

    /**
     * @author  Nicolas ZHOU
     * @author  Marion JURE
     * @author  Mathis QUERAULT
     * @author  Tristan LE SAUX
     */

    /**
     * Cette methode visite l'arbre d'une fonction de façon arbitraire :
     * Declaration -> Instruction
     * @param function
     * @return La valeur de la dernière instruction visitée.
     */
    @Override
    public T visit(Function function) {
        for (Declaration declaration: function.getParameters()) {
            declaration.accept(this);
        }

        List<Instruction> instructions = function.getInstructions();
        int size = instructions.size();
        for (int index = 0; index < size-1; index++) {
            instructions.get(index).accept(this);
        }
        return instructions.get(size-1).accept(this);
    }

    @Override
    public T visit(TypeDefinition typeDefinition) {
        List<Declaration> declarations = typeDefinition.getDeclarations();
        int size = declarations.size();
        for (int index = 0; index < size-1; index++) {
            declarations.get(index).accept(this);
        }
        return declarations.get(size-1).accept(this);
    }

    @Override
    public T visit(Import imports) {
        return null;
    }

    @Override
    public T visit(GlobalDeclaration globalDeclaration) {
        return globalDeclaration.getExpression().get().accept(this);
    }

    /**
     * Cette methode visite l'arbre du programme de façon arbitraire :
     * Imports -> GlobalDecelaration -> TypeDefinition -> Function
     * @param program
     * @return La valeur de la dernière fonction visitée
     */
    @Override
    public T visit(Program program){
        for (Import my_import : program.getImports()){
            my_import.accept(this);
        }
        for (GlobalDeclaration globalDeclaration : program.getGlobalDeclarations()){
            globalDeclaration.accept(this);
        }
        for (TypeDefinition typeDefinition:program.getTypeDefinitions()){
            typeDefinition.accept(this);
        }
        List<Function> functionList = program.getFunctions();
        int size = functionList.size();
        for (int index = 0; index < size-1 ; index++) {
            functionList.get(index).accept(this);
        }
        return functionList.get(size-1).accept(this);
    }

    // #####################################################################################################
    // # FIN Bloc Definition de fonction, définition de type, imports, déclarations globales et programmes #
    // #####################################################################################################

}
