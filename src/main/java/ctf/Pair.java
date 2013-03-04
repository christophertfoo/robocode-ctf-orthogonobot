package ctf;

/**
 * A tuple with two values.
 * 
 * @author Christopher Foo
 * 
 * @param <T1> The type of the first value.
 * @param <T2> The type of the second value.
 */
public class Pair<T1, T2> {
  private T1 value1;
  private T2 value2;

  /**
   * Creates a new {@link Pair} with the given starting values.
   * 
   * @param value1 The starting value of the first value in the Pair.
   * @param value2 The starting value of the second value in the Pair.
   */
  public Pair(T1 value1, T2 value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  /**
   * Gets the first value of this {@link Pair}.
   * @return The first value of this Pair.
   */
  public T1 getValue1() {
    return this.value1;
  }

  /**
   * Sets the first value of this {@link Pair} to the given value.
   * @param value1 The new value of the first value of this Pair.
   */
  public void setValue1(T1 value1) {
    this.value1 = value1;
  }

  /**
   * Gets the second value of this {@link Pair}.
   * @return The second value of this Pair.
   */
  public T2 getValue2() {
    return this.value2;
  }

  /**
   * Sets the second value of this {@link Pair} to the given value.
   * @param value2 The new value of the second value of this Pair.
   */
  public void setValue2(T2 value2) {
    this.value2 = value2;
  }

  /**
   * Determines if this {@link Pair} equals the given object.
   * 
   * @param obj The object to check against.
   * @return If the two objects are equal.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Pair)) {
      return false;
    }
    Pair<?, ?> other = (Pair<?, ?>) obj;
    boolean val1Eq = this.value1 == null ? other.value1 == null : this.value1.equals(other.value1);
    boolean val2Eq = this.value2 == null ? other.value2 == null : this.value2.equals(other.value2);
    return val1Eq && val2Eq;
  }

  /**
   * Finds the hash code of this {@link Pair}.
   * 
   * @return The hash code of this Pair.
   */
  @Override
  public int hashCode() {
    int result = 17;
    int fieldHash = this.value1 == null ? 0 : this.value1.hashCode();
    result = 31 * result + fieldHash;
    fieldHash = this.value2 == null ? 0 : this.value2.hashCode();
    result = 31 * result + fieldHash;
    return result;
  }
}
