package ctf;

import robocode.util.Utils;

/**
 * A point in a two-dimensional space. Essentially just a {@link Pair} with primitive doubles as
 * both values.
 * 
 * @author Christopher Foo
 * 
 */
public class Point {

  /**
   * The x coordinate of the {@link Point}.
   */
  private double x;

  /**
   * The y coordinate of the {@link Point}.
   */
  private double y;

  /**
   * Creates a new {@link Point} at the given coordinates.
   * 
   * @param x The x coordinate of the Point.
   * @param y The y coordinate of the Point.
   */
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Gets the current x coordinate of this {@link Point}.
   * 
   * @return This Point's current x coordinate.
   */
  public double getX() {
    return this.x;
  }

  /**
   * Sets the x coordinate of this {@link Point} to the specified value.
   * 
   * @param x The new x coordinate of this Point.
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * Gets the current y coordinate of this {@link Point}.
   * 
   * @return This Point's current y coordinate.
   */
  public double getY() {
    return this.y;
  }

  /**
   * Sets the y coordinate of this {@link Point} to the specified value.
   * 
   * @param y The new y coordinate of this Point.
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   * Determines if this {@link Point} equals the given object.
   * 
   * @param obj The object to check against.
   * @return If the two objects are equal.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Point)) {
      return false;
    }
    Point other = (Point) obj;
    return Utils.isNear(this.x, other.x) && Utils.isNear(this.y, other.y);
  }

  /**
   * Finds the hash code of this {@link Point}.
   * 
   * @return The hash code of this Point.
   */
  @Override
  public int hashCode() {
    int result = 17;
    long temp = Double.doubleToLongBits(this.x);
    int fieldHash = (int) (temp ^ (temp >>> 32));
    result = 31 * result + fieldHash;

    temp = Double.doubleToLongBits(this.y);
    fieldHash = (int) (temp ^ (temp >>> 32));
    result = 31 * result + fieldHash;
    return result;
  }

  @Override
  public String toString() {
    return "( " + this.x + ", " + this.y + ")";
  }
}
