package support;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListTools {

  public static <T1,T2> List<T1> getFstList(List<Pair<T1,T2>> listOfPairs){
    return listOfPairs.stream().map(Pair::getFst).collect(Collectors.toList());
  }

  public static <T1,T2> List<T2> getSndList(List<Pair<T1,T2>> listOfPairs){
    return listOfPairs.stream().map(Pair::getSnd).collect(Collectors.toList());
  }

  public static <T> List<T> singleton(T t){
    List<T> list = new ArrayList<T>();
    list.add(t);
    return list;
  }

  public static <T> List<T> two(T t1, T t2){
    List<T> list = new ArrayList<T>();
    list.add(t1);
    list.add(t2);
    return list;
  }

  public static String commaSeparatedList(List<String> list){
    StringBuilder result = new StringBuilder();
    for (int counter = 0; counter < list.size(); counter++) {
      result.append(list.get(counter));
      if (counter < list.size() - 1)
        result.append(", ");
    }
    return result.toString();
  }

  public static String stringFlatten(List<String> list){
    return list
      .stream()
      .reduce("", String::concat);
  }

  public static <T> List<T> flatten(List<List<T>> listOfLists){
    List<T> listOfT = new ArrayList<>();
    for(List<T> list : listOfLists)
      listOfT.addAll(list);
    return listOfT;
  }
}
