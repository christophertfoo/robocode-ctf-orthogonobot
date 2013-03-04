package ctf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

/**
 * Tests the methods of the {@link Pair} class.
 * 
 * @author Christopher Foo
 * 
 */
public class TestPair {

  /**
   * Tests the equals method of the {@link Pair} class.
   */
  @Test
  public void testEquals() {
    Pair<Integer, Integer> pair1 = new Pair<Integer, Integer>(10, 30);
    Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(10, 30);

    // Test for false negatives
    final String falseNegativeError = "Pairs with the same values should be equal.";
    assertEquals(falseNegativeError, pair1, pair2);
    assertEquals(falseNegativeError, pair2, pair1);

    pair1.setValue1(null);
    pair2.setValue1(null);
    assertEquals(falseNegativeError, pair1, pair2);
    assertEquals(falseNegativeError, pair2, pair1);

    Pair<Point, Point> pair3 = new Pair<Point, Point>(new Point(-2, 43), new Point(43, 21));
    Pair<Point, Point> pair4 = new Pair<Point, Point>(new Point(-2, 43), new Point(43, 21));

    assertEquals(falseNegativeError, pair3, pair4);
    assertEquals(falseNegativeError, pair4, pair3);

    pair3.setValue2(null);
    pair4.setValue2(null);
    assertEquals(falseNegativeError, pair3, pair4);
    assertEquals(falseNegativeError, pair4, pair3);

    pair3.setValue1(null);
    pair4.setValue1(null);
    assertEquals(falseNegativeError, pair3, pair4);
    assertEquals(falseNegativeError, pair4, pair3);

    // Test for false positives
    final String falsePositiveError = "Pair with different values should not be equal.";
    pair1.setValue1(10);
    pair2.setValue1(10);
    pair1.setValue2(5);
    pair2.setValue2(4);
    assertNotEquals(falsePositiveError, pair1, pair2);
    assertNotEquals(falsePositiveError, pair2, pair1);

    pair2.setValue2(null);
    assertNotEquals(falsePositiveError, pair1, pair2);
    assertNotEquals(falsePositiveError, pair2, pair1);

    pair1.setValue1(null);
    assertNotEquals(falsePositiveError, pair1, pair2);
    assertNotEquals(falsePositiveError, pair2, pair1);

    pair3 = new Pair<Point, Point>(new Point(-2, -10), new Point(4, 7));
    pair4 = new Pair<Point, Point>(new Point(-2, -10), new Point(4, 6));
    assertNotEquals(falsePositiveError, pair3, pair4);
    assertNotEquals(falsePositiveError, pair4, pair3);

    pair4.getValue2().setY(7);
    pair3.getValue1().setX(0);
    assertNotEquals(falsePositiveError, pair3, pair4);
    assertNotEquals(falsePositiveError, pair4, pair3);

    // Test null and different classes
    assertNotEquals("Should not equal null.", pair1, null);

    assertNotEquals("Should not equal a different class.", pair3, new Point(2, 3));

    pair3.setValue1(new Point(0, 0));
    assertNotEquals("Should not equal a different class.", pair3, new Pair<Point, Integer>(
        new Point(0, 0), 5));
  }

  /**
   * Tests the hashCode method of the {@link Pair} class.
   */
  @Test
  public void testHashCode() {
    Pair<Integer, Integer> pair1 = new Pair<Integer, Integer>(10, 4);
    Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(10, 4);
    final String errorMessage = "Equal pairs should have the same hash code.";
    assertEquals(errorMessage, pair1.hashCode(), pair2.hashCode());

    pair1.setValue1(-2);
    pair2.setValue1(-2);
    assertEquals(errorMessage, pair1.hashCode(), pair2.hashCode());

    Pair<Point, Point> pair3 = new Pair<Point, Point>(new Point(1, 2), new Point(5, -3));
    Pair<Point, Point> pair4 = new Pair<Point, Point>(new Point(1, 2), new Point(5, -3));
    assertEquals(errorMessage, pair3.hashCode(), pair4.hashCode());
  }
}
