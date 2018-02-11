/**
 * The type Pair.
 *
 * @param <T> the type parameter
 * @param <U> the type parameter
 */
class Pair<T, U> {
  /**
   * The First.
   */
  final T first;
  /**
   * The Second.
   */
  final U second;

  /**
   * Instantiates a new Pair.
   *
   * @param first  the first
   * @param second the second
   */
  public Pair(T first, U second) {
    this.first = first;
    this.second = second;
  }
}
