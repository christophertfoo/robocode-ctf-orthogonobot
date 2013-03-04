package ctf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import robocode.util.Utils;

/**
 * Tests the methods in the {@link Helpers} class.
 * 
 * @author Christopher Foo
 * 
 */
public class TestHelpers {

  /**
   * Tests the calculateLocation method from the {@link Helpers} class.
   */
  @Test
  public void testCalculateLocation() {
    // Test Y Axis
    final String errorMessage = "Resulting location is not the expected value.";
    assertEquals(errorMessage, new Point(0, 29), Helpers.calculateLocation(0, 29, 0, 0));
    assertEquals(errorMessage, new Point(0, 40), Helpers.calculateLocation(0, 4, 0, 36));
    assertEquals(errorMessage, new Point(0, 0), Helpers.calculateLocation(0, 10, 0, -10));

    assertEquals(errorMessage, new Point(0, -29), Helpers.calculateLocation(180, 29, 0, 0));
    assertEquals(errorMessage, new Point(0, 32), Helpers.calculateLocation(180, 4, 0, 36));
    assertEquals(errorMessage, new Point(0, -20), Helpers.calculateLocation(180, 10, 0, -10));

    // Test X Axis
    assertEquals(errorMessage, new Point(29, 0), Helpers.calculateLocation(90, 29, 0, 0));
    assertEquals(errorMessage, new Point(40, 0), Helpers.calculateLocation(90, 4, 36, 0));
    assertEquals(errorMessage, new Point(0, 0), Helpers.calculateLocation(90, 10, -10, 0));

    assertEquals(errorMessage, new Point(-29, 0), Helpers.calculateLocation(270, 29, 0, 0));
    assertEquals(errorMessage, new Point(32, 0), Helpers.calculateLocation(270, 4, 36, 0));
    assertEquals(errorMessage, new Point(-20, 0), Helpers.calculateLocation(270, 10, -10, 0));

    // Test Q1
    Point returned = Helpers.calculateLocation(1, 4, 33, 65);
    assertTrue("Predicted X should be larger than the original if angle is in Q1.",
        returned.getX() > 33);
    assertTrue("Predicted Y should be larger than the original if angle is in Q1.",
        returned.getY() > 65);
    
    returned = Helpers.calculateLocation(89, 40, -5, -70);
    assertTrue("Predicted X should be larger than the original if angle is in Q1.",
        returned.getX() > -5);
    assertTrue("Predicted Y should be larger than the original if angle is in Q1.",
        returned.getY() > -70);

    // Test Q2
    returned = Helpers.calculateLocation(91, 21, 50, 43);
    assertTrue("Predicted X should be larger than the original if angle is in Q2.",
        returned.getX() > 50);
    assertTrue("Predicted Y should be smaller than the original if angle is in Q2.",
        returned.getY() < 43);
    
    returned = Helpers.calculateLocation(179, 5, -43, -80);
    assertTrue("Predicted X should be larger than the original if angle is in Q2.",
        returned.getX() > -43);
    assertTrue("Predicted Y should be smaller than the original if angle is in Q2.",
        returned.getY() < -80);

    // Test Q3
    returned = Helpers.calculateLocation(181, 3, 9, 3);
    assertTrue("Predicted X should be smaller than the original if angle is in Q3.",
        returned.getX() < 9);
    assertTrue("Predicted Y should be smaller than the original if angle is in Q3.",
        returned.getY() < 3);
    
    returned = Helpers.calculateLocation(269, 1, -3, -1);
    assertTrue("Predicted X should be smaller than the original if angle is in Q3.",
        returned.getX() < -3);
    assertTrue("Predicted Y should be smaller than the original if angle is in Q3.",
        returned.getY() < -1);

    // Test Q4
    returned = Helpers.calculateLocation(271, 9, 4, 3);
    assertTrue("Predicted X should be smaller than the original if angle is in Q4.",
        returned.getX() < 4);
    assertTrue("Predicted Y should be larger than the original if angle is in Q4.",
        returned.getY() > 3);
    
    returned = Helpers.calculateLocation(359, 29, -8, -11);
    assertTrue("Predicted X should be smaller than the original if angle is in Q4.",
        returned.getX() < -8);
    assertTrue("Predicted Y should be larger than the original if angle is in Q4.",
        returned.getY() > -11);
  }

  /**
   * Tests the getBearing method from the {@link Helpers} class.
   */
  @Test
  public void testGetBearing() {
    Point start;
    Point end;

    final String yIncError = "Points on the Y Axis and increasing should get a bearing of 0.";
    final String yDecError = "Points on the Y Axis and decreasing should get a bearing of 180.";
    final String xIncError = "Points on the X Axis and increasing should get a bearing of 90.";
    final String xDecError = "Points on the X Axis and decreasing should get a bearing of 270";
    final String q1Error =
        "Points going up and to the right should get a bearing between 0 and 90.";
    final String q2Error =
        "Points going down and to the right should get a bearing between 90 and 180";
    final String q3Error =
        "Points going down and to the left should get a bearing between 180 and 270";
    final String q4Error =
        "Points going up and to the left should get a bearing between 270 and 360";

    // Test Y Axis, increasing
    start = new Point(0, 5);
    end = new Point(0, 10);
    assertTrue(yIncError, Utils.isNear(0, Helpers.getBearing(start, end)));

    start.setY(0);
    end.setY(20);
    assertTrue(yIncError, Utils.isNear(0, Helpers.getBearing(start, end)));

    start.setY(-10);
    end.setY(0);
    assertTrue(yIncError, Utils.isNear(0, Helpers.getBearing(start, end)));

    start.setY(-20);
    end.setY(-10);
    assertTrue(yIncError, Utils.isNear(0, Helpers.getBearing(start, end)));

    // Test Y Axis, decreasing
    start.setY(10);
    end.setY(5);
    assertTrue(yDecError, Utils.isNear(180, Helpers.getBearing(start, end)));

    start.setY(1);
    end.setY(0);
    assertTrue(yDecError, Utils.isNear(180, Helpers.getBearing(start, end)));

    start.setY(0);
    end.setY(-1);
    assertTrue(yDecError, Utils.isNear(180, Helpers.getBearing(start, end)));

    start.setY(-10);
    end.setY(-20);
    assertTrue(yDecError, Utils.isNear(180, Helpers.getBearing(start, end)));

    // Test X Axis, increasing
    start.setX(5);
    start.setY(0);
    end.setX(10);
    end.setY(0);
    assertTrue(xIncError, Utils.isNear(90, Helpers.getBearing(start, end)));

    start.setX(0);
    end.setX(1);
    assertTrue(xIncError, Utils.isNear(90, Helpers.getBearing(start, end)));

    start.setX(-1);
    end.setX(0);
    assertTrue(xIncError, Utils.isNear(90, Helpers.getBearing(start, end)));

    start.setX(-20);
    end.setX(-10);
    assertTrue(xIncError, Utils.isNear(90, Helpers.getBearing(start, end)));

    // Test X Axis, decreasing
    start.setX(15);
    end.setX(10);
    assertTrue(xDecError, Utils.isNear(270, Helpers.getBearing(start, end)));

    start.setX(1);
    end.setX(0);
    assertTrue(xDecError, Utils.isNear(270, Helpers.getBearing(start, end)));

    start.setX(0);
    end.setX(-1);
    assertTrue(xDecError, Utils.isNear(270, Helpers.getBearing(start, end)));

    start.setX(-32);
    end.setX(-33);
    assertTrue(xDecError, Utils.isNear(270, Helpers.getBearing(start, end)));

    // Test Q1
    double result;
    start.setX(0);
    start.setY(0);
    end.setX(10);
    end.setY(23);
    result = Helpers.getBearing(start, end);
    assertTrue(q1Error, result > 0 && result < 90);

    start.setX(12);
    start.setY(13);
    end.setX(114);
    end.setY(15);
    result = Helpers.getBearing(start, end);
    assertTrue(q1Error, result > 0 && result < 90);

    start.setX(-5);
    start.setY(3);
    end.setX(-1);
    end.setY(8);
    result = Helpers.getBearing(start, end);
    assertTrue(q1Error, result > 0 && result < 90);

    start.setX(4);
    start.setY(-5);
    end.setX(7);
    end.setY(-1);
    result = Helpers.getBearing(start, end);
    assertTrue(q1Error, result > 0 && result < 90);

    start.setX(-3);
    start.setY(-9);
    end.setX(-1);
    end.setY(-8);
    result = Helpers.getBearing(start, end);
    assertTrue(q1Error, result > 0 && result < 90);

    // Test Q2
    start.setX(0);
    start.setY(0);
    end.setX(4);
    end.setY(-1);
    result = Helpers.getBearing(start, end);
    assertTrue(q2Error, result > 90 && result < 180);

    start.setX(12);
    start.setY(13);
    end.setX(41);
    end.setY(11);
    result = Helpers.getBearing(start, end);
    assertTrue(q2Error, result > 90 && result < 180);

    start.setX(-5);
    start.setY(3);
    end.setX(-1);
    end.setY(2);
    result = Helpers.getBearing(start, end);
    assertTrue(q2Error, result > 90 && result < 180);

    start.setX(4);
    start.setY(-5);
    end.setX(7);
    end.setY(-6);
    result = Helpers.getBearing(start, end);
    assertTrue(q2Error, result > 90 && result < 180);

    start.setX(-3);
    start.setY(-9);
    end.setX(-1);
    end.setY(-10);
    result = Helpers.getBearing(start, end);
    assertTrue(q2Error, result > 90 && result < 180);

    // Test Q3
    start.setX(0);
    start.setY(0);
    end.setX(-4);
    end.setY(-1);
    result = Helpers.getBearing(start, end);
    assertTrue(q3Error, result > 180 && result < 270);

    start.setX(12);
    start.setY(13);
    end.setX(9);
    end.setY(11);
    result = Helpers.getBearing(start, end);
    assertTrue(q3Error, result > 180 && result < 270);

    start.setX(-5);
    start.setY(3);
    end.setX(-9);
    end.setY(2);
    result = Helpers.getBearing(start, end);
    assertTrue(q3Error, result > 180 && result < 270);

    start.setX(4);
    start.setY(-5);
    end.setX(2);
    end.setY(-6);
    result = Helpers.getBearing(start, end);
    assertTrue(q3Error, result > 180 && result < 270);

    start.setX(-3);
    start.setY(-9);
    end.setX(-4);
    end.setY(-10);
    result = Helpers.getBearing(start, end);
    assertTrue(q3Error, result > 180 && result < 270);

    // Test Q4
    start.setX(0);
    start.setY(0);
    end.setX(-4);
    end.setY(1);
    result = Helpers.getBearing(start, end);
    assertTrue(q4Error, result > 270 && result < 360);

    start.setX(12);
    start.setY(13);
    end.setX(9);
    end.setY(14);
    result = Helpers.getBearing(start, end);
    assertTrue(q4Error, result > 270 && result < 360);

    start.setX(-5);
    start.setY(3);
    end.setX(-9);
    end.setY(23);
    result = Helpers.getBearing(start, end);
    assertTrue(q4Error, result > 270 && result < 360);

    start.setX(4);
    start.setY(-5);
    end.setX(2);
    end.setY(-1);
    result = Helpers.getBearing(start, end);
    assertTrue(q4Error, result > 270 && result < 360);

    start.setX(-3);
    start.setY(-9);
    end.setX(-4);
    end.setY(-2);
    result = Helpers.getBearing(start, end);
    assertTrue(q4Error, result > 270 && result < 360);
  }
}
