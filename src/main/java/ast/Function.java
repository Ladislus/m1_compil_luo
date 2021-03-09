package ast;

import java.util.ArrayList;
import java.util.List;

public class Function extends Node{

    private List<Instruction> instructions;
    private List<Declaration> parameters;
    private EnumVisibility visibility;
    private Type return_type;

    //La fonction quand elle a une instruction mais pas de paramètres
    public Function(Instruction instruction, Type return_type) {
        List<Instruction> listInstruction = new ArrayList<Instruction>();
        listInstruction.add(instruction);
        this.instructions = listInstruction;
        this.visibility=EnumVisibility.PUBLIC;
        this.return_type=return_type;
    }

    public Function(List<Instruction> instructions, Type return_type) {
        this.instructions = instructions;
        this.visibility=EnumVisibility.PUBLIC;
        this.return_type=return_type;
    }
    //La fonction quand elle a une instruction et un paramètre
    public Function(Instruction instruction, Declaration parameter, Type return_type) {
        List<Instruction> listInstruction = new ArrayList<Instruction>();
        listInstruction.add(instruction);
        this.instructions = listInstruction;
        List<Declaration> listParameter = new ArrayList<Declaration>();
        listParameter.add(parameter);
        this.parameters = listParameter;
        this.visibility=EnumVisibility.PUBLIC;
        this.return_type=return_type;
    }

    public Function(List<Instruction> instructions, Declaration declaration, Type return_type) {
        this.instructions = instructions;
        List<Declaration> listDeclaration = new ArrayList<Declaration>();
        listDeclaration.add(declaration);
        this.parameters = listDeclaration;
        this.visibility=EnumVisibility.PUBLIC;
        this.return_type=return_type;
    }

    public Function(List<Instruction> instructions, List<Declaration> declarations, Type return_type) {
        this.instructions = instructions;
        this.parameters=declarations;
        this.visibility=EnumVisibility.PUBLIC;
        this.return_type=return_type;
    }

    public Function(Instruction instruction, Type return_type, EnumVisibility visibility) {
        List<Instruction> listInstruction = new ArrayList<Instruction>();
        listInstruction.add(instruction);
        this.instructions = listInstruction;
        this.visibility=visibility;
        this.return_type=return_type;
    }

    public Function(List<Instruction> instructions, Type return_type, EnumVisibility visibility) {
        this.instructions = instructions;
        this.visibility=visibility;
        this.return_type=return_type;
    }
    //La fonction quand elle a une instruction et un paramètre
    public Function(Instruction instruction, Declaration parameter, Type return_type, EnumVisibility visibility) {
        List<Instruction> listInstruction = new ArrayList<Instruction>();
        listInstruction.add(instruction);
        this.instructions = listInstruction;
        List<Declaration> listParameter = new ArrayList<Declaration>();
        listParameter.add(parameter);
        this.parameters = listParameter;
        this.visibility=visibility;
        this.return_type=return_type;
    }

    public Function(List<Instruction> instructions, Declaration declaration, Type return_type, EnumVisibility visibility) {
        this.instructions = instructions;
        List<Declaration> listDeclaration = new ArrayList<Declaration>();
        listDeclaration.add(declaration);
        this.parameters = listDeclaration;
        this.visibility=visibility;
        this.return_type=return_type;
    }

    public Function(List<Instruction> instructions, List<Declaration> declarations, Type return_type, EnumVisibility visibility) {
        this.instructions = instructions;
        this.parameters=declarations;
        this.visibility=visibility;
        this.return_type=return_type;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
    public List<Declaration> getParameters(){ return parameters; }
    public Type getReturn_type(){ return return_type; }
    public EnumVisibility getVisibility(){ return visibility;}

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
