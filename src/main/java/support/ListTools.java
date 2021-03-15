package support;

import java.util.ArrayList;
import java.util.List;

public class ListTools {

  public static <T> List<T> singleton(T t){
    List<T> list = new ArrayList<T>();
    list.add(t);
    return list;
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
