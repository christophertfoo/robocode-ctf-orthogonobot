package ctf;

/**
 * 
 * @author Chris
 * 
 */
public class AverageEstimator implements VectorEstimator {

  @Override
  public Pair<Point, Point> estimate(Point... points) {
    double averageSlope = 0;
    double averageYInt = 0;

    // Should not be executed as validateVectorPoints should catch this case before running this
    // method.
    if (points.length < 2) {
      return null;
    }

    for (int i = 0; i < points.length - 1; i++) {
      double currentSlope =
          (points[i + 1].getY() - points[i].getY()) / (points[i + 1].getX() - points[i].getX());
      double currentYInt = points[i].getY() - points[i].getX() * currentSlope;
      averageSlope += currentSlope;
      averageYInt += currentYInt;
    }
    averageSlope /= points.length;
    averageYInt /= points.length;

    Point start = new Point(points[0].getX(), points[0].getX() * averageSlope + averageYInt);
    Point end =
        new Point(points[points.length - 1].getX(), points[points.length - 1].getX() * averageSlope
            + averageYInt);

    return new Pair<Point, Point>(start, end);
  }
}
