package semantic_analysis;

import ast.InsBlock;

import java.util.Stack;

public class VisitedBlocks {

    // Each time this visitor enters a block, it will push it on this
    // visitedBlocks stack. Each time it exits a block, it will pop
    // the stack. Therefore this stack contains at its top the current
    // visited block, and after its parent block, then its grand-parent
    // block, and so on.
    private final Stack<InsBlock> visitedBlocks;

    public VisitedBlocks() {
        this.visitedBlocks = new Stack<>();
    }

    public Stack<InsBlock> getStack(){
        return visitedBlocks;
    }

    public void enter(InsBlock b){
        visitedBlocks.push(b);
    }

    public void exit(){
        visitedBlocks.pop();
    }

    public InsBlock current(){
        return visitedBlocks.peek();
    }

}
