package ctf;

import robocode.util.Utils;

/**
 * Provides static methods to assist the {@link Orthogonobot}.
 * 
 * @author Christopher Foo
 * 
 */
public final class Helpers {

  /**
   * A private constructor to hide the default.
   */
  private Helpers() {
    // Empty to hide constructor.
  }

  /**
   * Calculates the location of an enemy {@link Robot} as a {@link Point}.
   * 
   * @param absBearing The absolute bearing to the enemy robot (relative bearing + this Robot's
   * heading).
   * @param enemyDistance The distance of the enemy.
   * @param currentX The current x coordinate of the scanning Robot.
   * @param currentY The current y coordinate of the scanning Robot.
   * @return The location of the enemy Robot.
   */
  public static Point calculateLocation(double absBearing, double enemyDistance, double currentX,
      double currentY) {
    double x;
    double y;
    double enemyBearing = Utils.normalAbsoluteAngleDegrees(absBearing);

    // If Parallel to Axes
    if (Utils.isNear(enemyBearing, 0)) {
      return new Point(currentX, currentY + enemyDistance);
    }
    else if (Utils.isNear(enemyBearing, 90)) {
      return new Point(currentX + enemyDistance, currentY);
    }
    else if (Utils.isNear(enemyBearing, 180)) {
      return new Point(currentX, currentY - enemyDistance);
    }
    else if (Utils.isNear(enemyBearing, 270)) {
      return new Point(currentX - enemyDistance, currentY);
    }

    // Quadrant 1
    if (enemyBearing < 90) {
      x = Math.sin(Math.toRadians(enemyBearing)) * enemyDistance;
      y = Math.cos(Math.toRadians(enemyBearing)) * enemyDistance;
    }

    // Quadrant 2
    else if (enemyBearing < 180) {
      x = Math.cos(Math.toRadians(enemyBearing - 90)) * enemyDistance;
      y = Math.sin(Math.toRadians(enemyBearing - 90)) * enemyDistance * -1;
    }

    // Quadrant 3
    else if (enemyBearing < 270) {
      x = Math.sin(Math.toRadians(enemyBearing - 180)) * enemyDistance * -1;
      y = Math.cos(Math.toRadians(enemyBearing - 180)) * enemyDistance * -1;
    }

    // Quadrant 4
    else {
      x = Math.cos(Math.toRadians(enemyBearing - 270)) * enemyDistance * -1;
      y = Math.sin(Math.toRadians(enemyBearing - 270)) * enemyDistance;
    }
    return new Point(x + currentX, y + currentY);
  }

  /**
   * Gets the angle between the two given {@link Point}s as a bearing (>= 0 && < 360).
   * 
   * @param start The Point to get the bearing from.
   * @param end The Point to get the bearing to.
   * @return The bearing from the start to the end.
   */
  public static double getBearing(Point start, Point end) {
    double xDirection = end.getX() - start.getX();
    double yDirection = end.getY() - start.getY();

    if (Utils.isNear(xDirection, 0)) {
      if (Utils.isNear(yDirection, 0) || yDirection > 0) {
        return 0;
      }
      else {
        return 180;
      }
    }

    double slope = yDirection / xDirection;
    if (xDirection >= 0 && yDirection >= 0) {
      return 90 - Math.abs(Math.toDegrees(Math.atan(slope)));
    }
    else if (xDirection >= 0 && yDirection <= 0) {
      return 90 + Math.abs(Math.toDegrees(Math.atan(slope)));
    }
    else if (xDirection <= 0 && yDirection <= 0) {
      return 270 - Math.abs(Math.toDegrees(Math.atan(slope)));
    }
    else {
      return 270 + Math.abs(Math.toDegrees(Math.atan(slope)));
    }
  }
}
