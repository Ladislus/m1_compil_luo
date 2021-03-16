package ast;


import support.Pair;

import java.util.Objects;

abstract // TODO: TO REMOVE LATER WHEN THE CLASS IS COMPLETE
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
    public T visit(ExpUnaryOperation operation) {
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
    public T visit(ExpArrayEnum enumeration) {
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
        for (Expression exp : function.getArguments()) curr = exp.accept(this);
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

    /** Authors
     * @author  Nicolas ZHOU
     * @author  Marion JURE
     * @author  Mathis QUERAULT
     * @author  Tristan LE SAUX
     **/

    /**
     * Cette methode visite l'arbre d'une fonction de façon arbitraire :
     * Declaration -> Instruction
     * @param function
     * @return La valeur du dernier enfant visité.
     */
    @Override
    public T visit(Function function) {
        T curr = null;
        for (Declaration parameter: function.getParameters())
            curr = parameter.accept(this);

        for (Declaration declaration: function.getBody().getDeclarations())
            curr = declaration.accept(this);
        for (Instruction instruction: function.getBody().getBody())
            curr = instruction.accept(this);

        //todo
        /*T visited = function.getBody().accept(this);
        if (!Objects.isNull(visited)){
            curr = visited;
        }*/
        return curr;
    }

    @Override
    public T visit(TypeDefinition typeDefinition) {
        T curr = null;
        for (Declaration declaration: typeDefinition.getDeclarations())
            curr = declaration.accept(this);
        return curr;
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
     * @return La valeur du dernier enfant visité
     */
    @Override
    public T visit(Program program){
        T curr = null;
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

    // ##################################
    // #    Block Declaration Locale    #
    // #################################
    /**
     * @author Letord Baptiste
     * @author Arnaud Pasquier
     * @author Habib Bayoussoula
     */

    public T visit(Declaration declaration){
        if(declaration.getExpression().isPresent()){
            declaration.getType().accept(this);
            return declaration.getExpression().get().accept(this);
        }
        else{
            return declaration.getType().accept(this);
        }
    }
    // ######################################
    // #    Fin Block Declaration Locale    #
    // ######################################

    // ##########################
    // #    Block Instruction   #
    // ##########################
    /**
     * @author BENAI Mahmoud
     * @author BONHOMME Hugo
     * @author BRAHIM AZIB Youssouf
     * @author VADET Alexandre
     */

    @Override
    public T visit(InsFor instruction) {
        instruction.getDeclaration().accept(this);
        instruction.getRange().accept(this);
        instruction.getStep().accept(this);
        return instruction.getBody().accept(this);

    }

    @Override
    public T visit(InstForeach instruction) {
        instruction.getCollection().accept(this);
        instruction.getType().accept(this);
        return instruction.getBody().accept(this);
    }

    @Override
    public T visit(InsWhile instruction) {
        if(instruction.getDoWhile()){
            instruction.getBody().accept(this);
            return instruction.getCondition().accept(this);
        }
        else{
            instruction.getCondition().accept(this);
            return instruction.getBody().accept(this);
        }

    }

    @Override
    public T visit(InsIf instruction) {
        instruction.getCondition().accept(this);
        T curr = null;
        if(instruction.getElseif().isEmpty()){
            if(instruction.getBodyElse().isPresent() == false){
                return instruction.getBody().accept(this);
            }
            else{
                instruction.getBody().accept(this);
                return instruction.getBodyElse().get().accept(this);
            }
        }
        else{
            instruction.getBody().accept(this);
            for(Pair pair : instruction.getElseif()){
                curr = pair.getFst().accept(this);
                curr = pair.getSnd().accept(this);
            }
            if(instruction.getBodyElse().isPresent() == false){
                return curr;
            }
            else{
                return instruction.getBodyElse().get().accept(this);
            }
        }
    }

    @Override
    public T visit(InsAssign instruction) {
        instruction.getlValue().accept(this);
        return instruction.getExpression().accept(this);
    }

    @Override
    public T visit(InsBlock instruction) {
        T curr = null;
        for(Declaration declaration : instruction.getDeclarations()){
            curr = declaration.accept(this);
        }
        for(Instruction instruction1 : instruction.getBody()){
            curr = instruction1.accept(this);
        }
        return curr;
    }

    @Override
    public T visit(InsBreak instruction) {
        return null;
    }

    @Override
    public T visit(InsExpression instruction) {
        return instruction.getExpression().accept(this);
    }

    @Override
    public T visit(InsReturn instruction) { return instruction.getExpression().accept(this);}

    // ##########################
    // #    Fin Instruction     #
    // ##########################

}
