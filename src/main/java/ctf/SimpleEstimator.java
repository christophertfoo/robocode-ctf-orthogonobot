package ctf;

/**
 * A very simple {@link VectorEstimator} that estimates that the {@link MovementVector} should be
 * between the first and last {@link Point}s.
 * 
 * @author Christopher Foo
 * 
 */
public class SimpleEstimator implements VectorEstimator {

  /**
   * Estimates the starting and the ending {@link Point}s of a {@link MovementVector} that matches
   * the given Points. Assumes that the Points are valid as determined by
   * {@link MovementVector#validVectorPoints}.
   * 
   * Just chooses the first and last points in the given set of Points.
   * 
   * @param points The Points used to estimate the MovementVector.
   * @return A {@link Pair} containing the starting (retrieve with {@link Pair#getValue1()}) and
   * ending (retrieve with {@link Pair#getValue2()) Points of the estimated linear MovementVector.
   */
  @Override
  public Pair<Point, Point> estimate(Point... points) {
    // Should not be executed as it should be caught by validVectorPoints.
    if (points.length < 2) {
      return null;
    }
    return new Pair<Point, Point>(points[0], points[points.length - 1]);
  }
}
