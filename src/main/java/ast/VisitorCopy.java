package ast;

import java.util.ArrayList;
import java.util.List;

public class VisitorCopy implements Visitor<Node>{


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
    public Node visit(ExpPreUnaryOperation operation) {
        return new ExpPreUnaryOperation(operation.getExpression(), operation.getOperator()).getExpression().accept(this);
    }

    @Override
    public Node visit(ExpPostUnaryOperation operation) {
        return new ExpPostUnaryOperation(operation.getExpression(), operation.getOperator()).getExpression().accept(this);
    }

    @Override
    public Node visit(ExpBinaryOperation operation) {
        ExpBinaryOperation no = new ExpBinaryOperation(operation.getLeft(), operation.getOperator(), operation.getRight());
        no.getLeft().accept(this);
        return no.getRight().accept(this);
    }

    @Override
    public Node visit(ExpBoolean bool) {
        return null;
    }

    @Override
    public Node visit(ExpCharacter character) {
        return null;
    }

    @Override
    public Node visit(ExpInteger integer) {
        return null;
    }

    @Override
    public Node visit(ExpVariable variable) {
        return null;
    }

    @Override
    public Node visit(ExpRecordAccess record) {
        return new ExpRecordAccess(record.getRecord(), new String(record.getField())).getRecord().accept(this);
    }

    @Override
    public Node visit(ExpArrayAccess array) {
        ExpArrayAccess na = new ExpArrayAccess(array.getArray(), array.getIndex());
        na.getArray().accept(this);
        return na.getIndex().accept(this);
    }

    @Override
    public Node visit(ExpEnum enumeration) {
        ExpEnum ne = new ExpEnum(new ArrayList<>(enumeration.getElements()));
        Node curr = null;
        for (Expression exp : ne.getElements()) curr = exp.accept(this);
        return curr;
    }

    @Override
    public Node visit(ExpTuple tuple) {
        ExpTuple nt = new ExpTuple(tuple.getFirst(), tuple.getSecond());
        nt.getFirst().accept(this);
        return nt.getSecond().accept(this);
    }

    @Override
    public Node visit(ExpFunctionCall function) {
        ExpFunctionCall nf = new ExpFunctionCall(new String(function.getFunction()), new ArrayList<>(function.getArgs()));
        Node curr = null;
        for (Expression exp : nf.getArgs()) curr = exp.accept(this);
        return curr;
    }

    @Override
    public Node visit(ExpString string) {
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
    public Node visit(TypPrimitive typPrimitive) {
        return null;
    }

    @Override
    public Node visit(TypDico typDictionary) {
        return  new TypDico(typDictionary.getType()).getType().accept(this);
    }

    @Override
    public Node visit(TypVariable typRecord) {
        return null;
    }

    @Override
    public Node visit(TypArray typArray) {
        return new TypArray(typArray.getType()).getType().accept(this);
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

    @Override
    public Node visit(Function function) {
        List<Declaration> declarations = new ArrayList<>();
        for (Declaration declaration : function.getParameters()){
            declarations.add((Declaration) declaration.accept(this));
        }

        List<Instruction> instructions = new ArrayList<>();
        for (Instruction instruction : function.getInstructions()){
            instructions.add((Instruction) instruction.accept(this));
        }

        Type type = (Type) function.getReturn_type().accept(this);

        return  new Function(instructions,declarations,type,function.getVisibility());
    }

    @Override
    public Node visit(TypeDefinition typeDefinition) {
        List<Declaration> declarations = new ArrayList<>();;
        for (Declaration declaration : typeDefinition.getDeclarations()){
            declarations.add((Declaration) declaration.accept(this));
        }
        return  new TypeDefinition(new String(typeDefinition.getName()),declarations);
    }

    @Override
    public Node visit(Import imports) {
        return  new Import(new String(imports.getPath()));
    }

    @Override
    public Node  visit(GlobalDeclaration globalDeclaration) {
        Expression expression = (Expression) globalDeclaration.getExpression().get().accept(this);
        Type type =(Type) globalDeclaration.getType().accept(this);
        return new GlobalDeclaration(globalDeclaration.getVisibility(), type, new String(globalDeclaration.getVariable()), expression);
    }

    @Override
    public Node visit(Program program){
        List<Import> importList = new ArrayList<>();;
        for (Import  my_import : program.getImports()){
            importList.add((Import) my_import.accept(this));
        }

        List<GlobalDeclaration > globalDeclarationList = new ArrayList<>();;
        for (GlobalDeclaration globalDeclaration : program.getGlobalDeclarations()){
            globalDeclarationList.add((GlobalDeclaration) globalDeclaration.accept(this));
        }

        List<TypeDefinition> typeDefinitionList= new ArrayList<>();
        for (TypeDefinition typeDefinition:program.getTypeDefinitions()){
           typeDefinitionList.add((TypeDefinition) typeDefinition.accept(this));
        }
        List<Function> functionList = new ArrayList<>();
        for (Function function:program.getFunctions()){
            functionList.add((Function) function.accept(this));
        }

        return new Program(importList,globalDeclarationList,typeDefinitionList,functionList);
    }

    // #####################################################################################################
    // # FIN Bloc Definition de fonction, définition de type, imports, déclarations globales et programmes #
    // #####################################################################################################

}
