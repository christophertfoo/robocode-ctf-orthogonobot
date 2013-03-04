package ctf;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests the methods in the {@link SimpleEstimator} class.
 * @author Christopher Foo
 * 
 */
public class TestSimpleEstimator {

  /**
   * Tests the estimate method of the {@link SimpleEstimator} class.
   */
  @Test
  public void testEstimate() {
    SimpleEstimator estimator = new SimpleEstimator();
    final String errorMessage = "Should match the first and last points.";
    
    // Test Q1
    Point[] testArray1 = { new Point(-1, 1), new Point(4, 3), new Point(7, 4), new Point(10, 40) };
    assertEquals(errorMessage, new Pair<Point, Point>(testArray1[0],
        testArray1[testArray1.length - 1]), estimator.estimate(testArray1));

    // Test Q2
    Point[] testArray2 =
        { new Point(-1, 1), new Point(4, 0), new Point(7, -1), new Point(10, -49) };
    assertEquals(errorMessage, new Pair<Point, Point>(testArray2[0],
        testArray2[testArray2.length - 1]), estimator.estimate(testArray2));
    
    // Test Q3
    Point[] testArray3 = { new Point(5, 3), new Point(4, 2), new Point(1, 1), new Point(-60, -2) };
    assertEquals(errorMessage, new Pair<Point, Point>(testArray3[0],
        testArray3[testArray3.length - 1]), estimator.estimate(testArray3));

    // Test Q4
    Point[] testArray4 = { new Point(5, -9), new Point(4, -6), new Point(1, 1), new Point(-60, 8) };
    assertEquals(errorMessage, new Pair<Point, Point>(testArray4[0],
        testArray4[testArray4.length - 1]), estimator.estimate(testArray4));
  }
}
