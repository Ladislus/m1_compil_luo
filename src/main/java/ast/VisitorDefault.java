package ast;

import java.util.List;

abstract // TODO: TO REMOVE LATER WHEN THE CLASS IS COMPLETE
public class VisitorDefault<T> implements Visitor<T> {

    private T defaultValue;

    public VisitorDefault(T defaultValue) {
        this.defaultValue = defaultValue;
    }

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
        return this.defaultValue;
    }

    @Override
    public T visit(ExpCharacter character) {
        return this.defaultValue;
    }

    @Override
    public T visit(ExpInteger integer) {
        return this.defaultValue;
    }

    @Override
    public T visit(ExpVariable variable) {
        return this.defaultValue;
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
        T curr = this.defaultValue;
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
        T curr = this.defaultValue;
        for (Expression exp : function.getArguments()) curr = exp.accept(this);
        return curr;
    }

    @Override
    public T visit(ExpString string) {
        return this.defaultValue;
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
        return defaultValue;
    }

    @Override
    public T visit(TypDico typDictionary) {
        return typDictionary.getType().accept(this);
    }

    @Override
    public T visit(TypVariable typRecord) {
        return defaultValue;
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
        T curr = defaultValue;
        for (Declaration declaration: function.getParameters())
            curr = declaration.accept(this);
        for (Instruction instruction: function.getInstructions())
            curr = instruction.accept(this);
        return curr;
    }

    @Override
    public T visit(TypeDefinition typeDefinition) {
        T curr = defaultValue;
        for (Declaration declaration: typeDefinition.getDeclarations())
            curr = declaration.accept(this);
        return curr;
    }

    @Override
    public T visit(Import imports) {
        return defaultValue;
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
        T curr = defaultValue;
        for (Import my_import : program.getImports())
            curr = my_import.accept(this);
        for (GlobalDeclaration globalDeclaration : program.getGlobalDeclarations())
            curr = globalDeclaration.accept(this);
        for (TypeDefinition typeDefinition:program.getTypeDefinitions())
            curr = typeDefinition.accept(this);
        for (Function function: program.getFunctions())
            curr = function.accept(this);
        return curr;
    }

    // #####################################################################################################
    // # FIN Bloc Definition de fonction, définition de type, imports, déclarations globales et programmes #
    // #####################################################################################################
}
