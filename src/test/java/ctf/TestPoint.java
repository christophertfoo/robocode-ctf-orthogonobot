package ctf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

/**
 * Tests the methods of the {@link Point} class.
 * 
 * @author Christopher Foo
 * 
 */
public class TestPoint {

  /**
   * Tests the equals method of the {@link Point} class.
   */
  @Test
  public void testEquals() {
    Point point1 = new Point(0, 0);
    Point point2 = new Point(0, 0);

    // Test for false negatives
    final String falseNegativeError = "Points with the same x and y values should be equal.";
    assertEquals(falseNegativeError, point1, point2);
    assertEquals(falseNegativeError, point2, point1);

    point1.setX(1);
    point2.setX(1);
    point1.setY(23);
    point2.setY(23);
    assertEquals(falseNegativeError, point1, point2);
    assertEquals(falseNegativeError, point2, point1);

    point1.setX(1);
    point2.setX(1);
    point1.setY(-4);
    point2.setY(-4);
    assertEquals(falseNegativeError, point1, point2);
    assertEquals(falseNegativeError, point2, point1);

    point1.setX(-1);
    point2.setX(-1);
    point1.setY(-4);
    point2.setY(-4);
    assertEquals(falseNegativeError, point1, point2);
    assertEquals(falseNegativeError, point2, point1);

    point1.setX(-6);
    point2.setX(-6);
    point1.setY(19);
    point2.setY(19);
    assertEquals(falseNegativeError, point1, point2);
    assertEquals(falseNegativeError, point2, point1);

    // Test for false positives
    final String falsePositiveError = "Points with different x and y values should not be equal.";
    point1.setX(1);
    point2.setX(0);
    point1.setY(-4);
    point2.setY(-4);
    assertNotEquals(falsePositiveError, point1, point2);
    assertNotEquals(falsePositiveError, point2, point1);

    point1.setX(1);
    point2.setX(1);
    point1.setY(-4);
    point2.setY(-3);
    assertNotEquals(falsePositiveError, point1, point2);
    assertNotEquals(falsePositiveError, point2, point1);

    // Test null and different classes
    assertNotEquals("Should not equal null.", point1, null);

    assertNotEquals("Should not equal a different class.", point1, new Pair<Double, Double>(1.0,
        -4.0));
  }

  /**
   * Tests the hashCode method of the {@link Point} class.
   */
  @Test
  public void testHashCode() {
    Point point1 = new Point(0, 0);
    Point point2 = new Point(0, 0);
    final String errorMessage = "Equal points should have the same hash code.";
    assertEquals(errorMessage, point1.hashCode(),
        point2.hashCode());
    
    point1.setX(321);
    point2.setX(321);
    point1.setY(812);
    point2.setY(812);
    assertEquals(errorMessage, point1.hashCode(),
        point2.hashCode());
    
    point1.setX(803);
    point2.setX(803);
    point1.setY(-1923);
    point2.setY(-1923);
    assertEquals(errorMessage, point1.hashCode(),
        point2.hashCode());
    
    point1.setX(-7);
    point2.setX(-7);
    point1.setY(-20312);
    point2.setY(-20312);
    assertEquals(errorMessage, point1.hashCode(),
        point2.hashCode());
    
    point1.setX(-93221);
    point2.setX(-93221);
    point1.setY(2);
    point2.setY(2);
    assertEquals(errorMessage, point1.hashCode(),
        point2.hashCode());
  }
}
