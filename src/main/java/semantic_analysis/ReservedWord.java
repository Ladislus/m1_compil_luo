package semantic_analysis;

import java.util.Collections;
import java.util.TreeSet;

public class ReservedWord {
    private static final TreeSet<String> reserved = buildReserved();

    private static TreeSet<String> buildReserved() {
        String[] reservedWords =
          { "new", "int_of_char", "char_of_int", "length"};
          //ToDo : check the list is complete for LUO
        TreeSet<String> reserved = new TreeSet<>();
        Collections.addAll(reserved, reservedWords);
        return reserved;
    }

    public static boolean is(String name) {
        return reserved.contains(name);
    }
}
