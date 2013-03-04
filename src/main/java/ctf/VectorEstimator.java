package ctf;

/**
 * Estimates a linear direction {@link MovementVector} based on some number of observed locations.
 * 
 * @author Christopher Foo
 * 
 */
public interface VectorEstimator {
  /**
   * Estimates the starting and ending point of a linear direction {@link MovementVector} that
   * appropriately represents the given {@link Point}s (NOTE: Assumes that the given Points are
   * valid as determined by {@link MovementVector#validVectorPoints}).
   * 
   * @param points The Points used to perform the estimation.
   * @return Returns a {@link Pair} containing the starting (retrieve with {@link Pair#getValue1})
   * and ending (retrieve with {@link Pair#getValue2}) Points of the estimated MovementVector.
   */
  public Pair<Point, Point> estimate(Point... points);
}
