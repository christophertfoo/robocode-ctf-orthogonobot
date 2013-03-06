package ctf;

/**
 * Contains all of the relevant information about an enemy {@link Robot}.
 * 
 * @author Christopher Foo
 * 
 */
public final class EnemyTuple {

  /**
   * The absolute bearing to the scanned enemy (the relative bearing + this {@link Orthogonobot}'s
   * heading).
   */
  private double absBearing;

  /**
   * The distance between this {@link Orthogonobot} and the scanned enemy.
   */
  private double distance;

  /**
   * The amount of energy that the scanned enemy has.
   */
  private double energy;

  /**
   * The {@link Point} the enemy is at.
   */
  private Point location;

  /**
   * Creates a new {@link EnemyTuple} with the given values.
   * 
   * @param absBearing The absolute bearing to the enemy (relative bearing + this
   * {@link Orthogonobot}'s heading).
   * @param distance The distance between this Orthogonobot and the enemy.
   * @param energy The amount of energy that the enemy has.
   * @param scannerX The x-coordinate of this Orthogonobot.
   * @param scannerY The y-coordinate of this Orthogonobot.
   */
  public EnemyTuple(double absBearing, double distance, double energy, double scannerX,
      double scannerY) {
    this.absBearing = absBearing;
    this.distance = distance;
    this.energy = energy;
    this.location = Helpers.calculateLocation(absBearing, distance, scannerX, scannerY);
  }

  /**
   * Gets the absolute bearing to the enemy (relative bearing + this {@link Orthogonobot}'s
   * heading).
   * 
   * @return The absolute bearing o the enemy.
   */
  public double getAbsBearing() {
    return this.absBearing;
  }

  /**
   * Gets the distance to the enemy.
   * 
   * @return The distance to the eney.
   */
  public double getDistance() {
    return this.distance;
  }

  /**
   * Gets the energy of the enemy.
   * 
   * @return The energy of the enemy.
   */
  public double getEnergy() {
    return this.energy;
  }
  
  /**
   * Gets the {@link Point} representing the enemy's location.
   * @return The enemy's location.
   */
  public Point getLocation() {
    return this.location;
  }
}