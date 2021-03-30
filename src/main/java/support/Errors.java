package support;

import java.util.LinkedList;
import java.util.List;

public class Errors
{
    private List<String> errors;

    public void add(String error){
        errors.add(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void print(){
        for (String error : errors)
            System.out.println(error);
    }

    public void reset(){
        errors = new LinkedList<>();
    }

    public Errors() {
        errors = new LinkedList<>();
    }
}
