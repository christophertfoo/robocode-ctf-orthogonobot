package ctf;

/**
 * A vector in a 2-D Cartesian Plane which represents the movement of an enemy.
 * 
 * @author Christopher Foo
 * 
 */
public class MovementVector {

  /**
   * The angle of the vector in degrees as if it were a bearing (0 degrees would be North, 90
   * degrees would be East, 180 degrees would be South, and 270 degrees would be West).
   */
  private double angle;
  /**
   * The average distance between each {@link Point} used to calculate this {@link MovementVector}.
   * Equal to the average velocity of the enemy.
   */
  private double averageDistance;

  /**
   * The {@link VectorEstimator} used to estimate the starting and ending {@link Point}s of this
   * {@link MovementVector}.
   */
  private VectorEstimator estimator;

  /**
   * The {@link Point} representing the last known location of the enemy.
   */
  private Point lastPoint;

  /**
   * Creates a new {@link MovementVector} based on the given values.
   * 
   * @param estimator The {@link VectorEstimator} used to estimate the linear MovementVector.
   * @param points The {@link Point}s used to estimate the MovementVector.
   */
  private MovementVector(VectorEstimator estimator, Point[] points) {
    this.estimator = estimator;
    this.performVectorUpdate(points);

  }

  /**
   * Creates a new {@link MovementVector} based on the given values.
   * 
   * @param estimator The {@link VectorEstimator} used to estimate the linear MovementVector.
   * @param points The {@link Point}s used to estimate the MovementVector.
   * 
   * @return The created Vector or null if the Vector could not be created.
   */
  public static MovementVector createVector(VectorEstimator estimator, Point... points) {
    if (estimator == null || !MovementVector.validVectorPoints(points)) {
      return null;
    }
    return new MovementVector(estimator, points);
  }

  /**
   * Determines if the given {@link Point}s could be used to generate a valid {@link MovementVector}
   * .
   * 
   * @param points The Points to be checked.
   * @return If the Points can be used to create a valid MovementVector.
   */
  public static boolean validVectorPoints(Point[] points) {
    if (points == null || points.length < 2) {
      return false;
    }

    if (points.length == 2) {
      return true;
    }

    double xDirection = points[1].getX() - points[0].getX();
    double yDirection = points[1].getY() - points[0].getY();
    for (int i = 2; i < points.length; i++) {

      // If the direction changes, multiplying the new direction by the old direction
      // would be negative.  If both directions changed, then the enemy changed direction
      // too abruptly and a MovementVector will not be accurate without further Points..
      if (!((points[i].getX() - points[i - 1].getX()) * xDirection < 0)
          && !((points[i].getY() - points[i - 1].getY()) * yDirection < 0)) {
        return false;
      }
    }

    // If all points pass, they make a valid MovementVector.
    return true;
  }

  /**
   * Updates this {@link MovementVector} to use the given {@link Point}s.
   * 
   * @param points The new Points that should be used to update this MovementVector.
   * @return If the update was successful or not.
   */
  public boolean updateVector(Point... points) {
    if (!MovementVector.validVectorPoints(points)) {
      return false;
    }
    this.performVectorUpdate(points);
    return true;
  }

  /**
   * Attempts to predict the enemy's location in the given number of turns using this
   * {@link MovementVector} .
   * 
   * @param numTurns The number of turns that the prediction is aimed at (i.e. a value of 4 would
   * predict the enemy's location in 4 turns). Should be > 0.
   * @return The {@link Point} representing the predicted location or null if there was an error.
   */
  public Point predictLocation(int numTurns) {
    if (numTurns < 0) {
      return null;
    }
    if (numTurns == 1) {
      return this.lastPoint;
    }
    Point returnPoint = this.lastPoint;
    for (int i = 0; i < numTurns; i++) {
      returnPoint = this.predictLocationOneTurn(returnPoint);
    }
    return new Point(0, 0);
  }

  /**
   * Predicts the enemy's location after one turn based on this {@link MovementVector} and the
   * enemy's last known location.
   * 
   * @param lastPoint The {@link Point} representing the enemy's last known location. Can be a
   * predicted location.
   * @return The enemy's predicted location after one turn.
   */
  private Point predictLocationOneTurn(Point lastPoint) {
    return Helpers.calculateLocation(this.angle, this.averageDistance, lastPoint.getX(),
        lastPoint.getY());
  }

  /**
   * Updates this {@link MovementVector} to use the given {@link Point}s (NOTE: Assumes that the
   * given Points pass the check by {@link #validVectorPoints}).
   * 
   * @param points The new Points that this MovementVector should use.
   */
  private void performVectorUpdate(Point... points) {
    Pair<Point, Point> vectorPoints = estimator.estimate(points);
    Point start = vectorPoints.getValue1();
    Point end = vectorPoints.getValue2();
    this.angle = Helpers.getBearing(start, end);
    this.findAverageDistance(start, end, points.length);
    this.lastPoint = end;
  }

  /**
   * Finds the average distance between the {@link Point}s of this {@link MovementVector}.
   * 
   * @param start The starting (first) Point of this MovementVector.
   * @param end The ending (last) Point of this MovementVector.
   * @param numElements The number of elements in this MovementVector.
   */
  private void findAverageDistance(Point start, Point end, int numElements) {
    if (numElements < 1) {
      return;
    }
    this.averageDistance =
        Math.hypot(end.getX() - start.getX(), end.getY() - start.getY()) / numElements;
  }
}
