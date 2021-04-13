package support;

import java.util.Objects;

public class Pair<T1, T2> {
  private T1 fst;
  private T2 snd;

  public T1 getFst() {
    return fst;
  }

  public T2 getSnd() {
    return snd;
  }

  public Pair(T1 fst, T2 snd) {
    this.fst = fst;
    this.snd = snd;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(fst, pair.fst) && Objects.equals(snd, pair.snd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fst, snd);
  }

  @Override
  public String toString() {
    return "Pair{" +
      "fst=" + fst +
      ", snd=" + snd +
      '}';
  }
}
