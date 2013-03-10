package ctf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import robocode.AdvancedRobot;
import robocode.BulletMissedEvent;
import robocode.Condition;
import robocode.CustomEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 * An {@link AdvancedRobot} that attempts to stay perpendicular to the nearest enemy to maximize
 * movement and hopefully stay out of the line-of-fire.
 * 
 * @author Christopher Foo
 * 
 */
public class Orthogonobot extends AdvancedRobot {

  /**
   * The distance that an {@link Orthogonobot} should stay from the edge of the battlefield.
   */
  private static final int EDGE_DISTANCE = 150;
  
  /**
   * The distance that an {@link Orthogonobot} should move when trying to dodge a bullet.
   */
  private static final double DODGE_AMOUNT = 40;

  /**
   * The {@link HashMap} containing the information about the enemy {@link Robots}.
   */
  private Map<String, ArrayList<EnemyTuple>> enemyMap =
      new HashMap<String, ArrayList<EnemyTuple>>();

  /**
   * The current velocity of this {@link Orthogonobot}.
   */
  private double currentVelocity = 50;

  /**
   * The current {@link BattleMode} of this {@link Orthogonobot}.
   */
  private BattleMode currentMode = BattleMode.NORMAL;

  /**
   * The amount that this {@link Orthogonobot} should turn its body in degrees.
   */
  private double bodyTurnAngle;

  /**
   * The amount that this {@link Orthogonobot} should turn its radar in degrees.
   */
  private double radarTurn = 180;

  /**
   * If this is the first time that this {@link Orthogonobot} is turning its radar.
   */
  private boolean initialRadarTurn = true;

  /**
   * If this {@link Orthogonobot} should try to lead its target. Will be disabled if it misses too
   * much.
   */
  private boolean leadTarget = true;

  /**
   * The number of shots that this {@link Orthogonobot} has fired in this round.
   */
  private long numShotsFired = 0;

  /**
   * The number of bullets that this {@link Orthogonobot} has fired, but missed the target.
   */
  private long numMisses = 0;

  /**
   * Where this {@link Orthogonobot} wants to move to assuming nothing happens (i.e. another bullet
   * is fired or it gets too close to the edge of the battlefield).
   */
  private Point destination = new Point(0, 0);

  /**
   * Determines the velocity that this {@link Orthogonobot} should take to dodge a fired bullet.
   * 
   * @param currentX The current X coordinate of this Orthogonobot.
   * @param currentY The current Y coordinate of this Orthogonobot.
   * @param currentHeading The current heading of this Orthogonobot.
   * @param robotWidth The width of this Orthogonobot.
   * @param robotHeight The height of this Orthogonobot.
   * @return The velocity that this Orthogonobot should use to move away from the enemy bullet.
   */
  private double determineVelocity(double currentX, double currentY, double currentHeading,
      double robotWidth, double robotHeight) {
    Point forwardDestination = Helpers.calculateLocation(currentHeading, 100, currentX, currentY);
    double forwardX = forwardDestination.getX();
    double forwardY = forwardDestination.getY();
    if ((forwardX > robotWidth + EDGE_DISTANCE || forwardX < this.getBattleFieldWidth()
        - EDGE_DISTANCE - robotWidth)
        && (forwardY > robotHeight + EDGE_DISTANCE || forwardY < this.getBattleFieldHeight()
            - EDGE_DISTANCE - robotHeight)) {
      return DODGE_AMOUNT;
    }
    else {
      return -1 * DODGE_AMOUNT;
    }
  }

  /**
   * Runs this {@link Orthogonobot}'s battle strategy.
   */
  @Override
  public void run() {

    final double robotWidth = this.getWidth() / 2;
    final double robotHeight = this.getHeight() / 2;
    this.setAllColors(Color.WHITE);
    this.setAdjustGunForRobotTurn(true);

    this.setTurnRadarRight(360);
    this.addCustomEvent(new NearEdgeCondition("nearEdge", this, EDGE_DISTANCE));
    while (true) {
      if (Utils.isNear(this.getRadarTurnRemaining(), 0)) {
        if (this.initialRadarTurn) {
          this.setTurnRadarRight(90);
          this.initialRadarTurn = false;
        }
        else {
          this.radarTurn *= -1;
          this.setTurnRadarRight(this.radarTurn);
        }
      }

      // Find closest enemy
      double shortestDistance = Double.MAX_VALUE;
      double shortestBearing = 0;
      double shortestDeltaEnergy = 0;
      double shortestEnergy = 0;
      List<EnemyTuple> shortestTupleList = null;
      for (String enemyName : this.enemyMap.keySet()) {
        List<EnemyTuple> enemyTupleList = this.enemyMap.get(enemyName);
        int tupleListSize = enemyTupleList.size();
        if (tupleListSize > 0) {
          EnemyTuple enemy = enemyTupleList.get(tupleListSize - 1);

          if (enemy.getDistance() < shortestDistance) {
            shortestDistance = enemy.getDistance();
            shortestBearing = enemy.getAbsBearing();
            shortestEnergy = enemy.getEnergy();
            shortestDeltaEnergy =
                tupleListSize >= 2 ? shortestEnergy
                    - enemyTupleList.get(tupleListSize - 2).getEnergy() : 100 - shortestEnergy;
            shortestTupleList = enemyTupleList;
          }
        }
      }

      // If the closest enemy cannot be found (i.e. did not scan an enemy yet), keep waiting.
      if (shortestTupleList == null) {
        this.execute();
        continue;
      }

      // If the enemyis almost dead, switch to Offensive mode to close in for the kill!
      if (shortestEnergy < 30 && this.getEnergy() > 40) {
        this.currentMode = BattleMode.OFFENSIVE;
      }

      // Choose movement based on the current mode
      switch (this.currentMode) {

      // Get away from the edge / corner of the battlefield.
      case NAVIGATION:
        if (Math.abs(this.getDistanceRemaining()) < 1) {
          this.currentMode = BattleMode.NORMAL;
          this.currentVelocity = Math.abs(this.currentVelocity);
        }
        break;

      // Stay somewhat perpendicular to the closest enemy
      case NORMAL:
        double currentX = this.getX();
        double currentY = this.getY();
        double currentHeading = this.getHeading();
        if (shortestDeltaEnergy < 0) {
          this.bodyTurnAngle = 0;
          this.currentVelocity =
              this.determineVelocity(currentX, currentY, currentHeading, robotWidth, robotHeight);
          this.setAhead(this.currentVelocity);
          this.destination =
              Helpers.calculateLocation(currentHeading, this.currentVelocity, currentX, currentY);
        }
        else {
          this.destination.setX(currentX);
          this.destination.setY(currentY);
          this.bodyTurnAngle =
              Utils.normalRelativeAngleDegrees(shortestBearing - this.getHeading() - 90);
        }

        break;

      // Close in for the kill!
      case OFFENSIVE:
        this.bodyTurnAngle = Utils.normalRelativeAngleDegrees(shortestBearing - this.getHeading());
        this.setAhead(Math.abs(this.currentVelocity));
        break;

      // Should never be reached.
      default:
        break;
      }
      this.setTurnRight(this.bodyTurnAngle);

      // Determine where to turn the gun
      double turnAngle;

      if (this.leadTarget && this.numShotsFired >= 10
          && ((double) this.numMisses) / this.numShotsFired > 0.5) {
        this.leadTarget = false;
      }

      turnAngle = Utils.normalRelativeAngleDegrees(shortestBearing - this.getGunHeading());
      this.setTurnGunRight(turnAngle);

      // Fire if there is enough energy and the gun is done turning.
      double currentEnergy = this.getEnergy();
      if (Utils.isNear(this.getGunTurnRemaining(), 0) || this.getGunTurnRemaining() < 2) {
        if (shortestDistance <= 50 && currentEnergy > 3) {
          this.setFire(3);
          this.numShotsFired++;
        }
        else if (shortestDistance <= 100 && currentEnergy > 2.5) {
          this.setFire(2.5);
          this.numShotsFired++;
        }
        else if (shortestDistance <= 150 && currentEnergy > 2) {
          this.setFire(2);
          this.numShotsFired++;
        }
        else if (currentEnergy > 1) {
          this.setFire(1);
          this.numShotsFired++;
        }
      }

      // Run everything!
      this.execute();
    }
  }

  /**
   * Triggered when a {@link CustomEvent} is triggered.
   * 
   * @param event The CustomEvent that was triggered.
   */
  @Override
  public void onCustomEvent(CustomEvent event) {
    Condition eventCondition = event.getCondition();

    if (eventCondition.getName().equals("nearEdge") && this.currentMode != BattleMode.NAVIGATION) {
      this.currentMode = BattleMode.NAVIGATION;
      switch (((NearEdgeCondition) eventCondition).getEdgeCase()) {
      case BOTTOM:
        this.bodyTurnAngle = Utils.normalRelativeAngleDegrees(-this.getHeading());
        break;
      case BOTTOM_LEFT:
        this.bodyTurnAngle = Utils.normalRelativeAngleDegrees(45 - this.getHeading());
        break;
      case BOTTOM_RIGHT:
        this.bodyTurnAngle = Utils.normalRelativeAngleDegrees(315 - this.getHeading());
        break;
      case LEFT:
        this.bodyTurnAngle = Utils.normalRelativeAngleDegrees(90 - this.getHeading());
        break;
      case NONE:
        this.currentMode = BattleMode.NORMAL;
        break;
      case RIGHT:
        this.bodyTurnAngle = Utils.normalRelativeAngleDegrees(270 - this.getHeading());
        break;
      case TOP:
        this.bodyTurnAngle = Utils.normalRelativeAngleDegrees(180 - this.getHeading());
        break;
      case TOP_LEFT:
        this.bodyTurnAngle = Utils.normalRelativeAngleDegrees(135 - this.getHeading());
        break;
      case TOP_RIGHT:
        this.bodyTurnAngle = Utils.normalRelativeAngleDegrees(225 - this.getHeading());
        break;
      default:
        break;
      }
      if (this.bodyTurnAngle > 90) {
        this.bodyTurnAngle -= 180;
        this.setAhead(-150);
      }

      else if (this.bodyTurnAngle < -90) {
        this.bodyTurnAngle += 180;
        this.setAhead(-150);
      }
      else {
        this.setAhead(150);
      }
    }
  }

  /**
   * Makes this {@link Orthogonobot} move in reverse when it hits a wall.
   * 
   * @param event The {@link HitWallEvent} containing the information about hitting the wall.
   */
  @Override
  public void onHitWall(HitWallEvent event) {
    if (this.currentMode != BattleMode.NAVIGATION) {
      out.println("OOOPS!");
    }
  }

  /**
   * Adds or updates the scanned enemy's information.
   * 
   * @param event The {@link ScannedRobotEvent} containing the scanned {@link Robot}'s information.
   */
  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    String enemyName = event.getName();
    if (!this.enemyMap.containsKey(enemyName)) {
      this.enemyMap.put(enemyName, new ArrayList<EnemyTuple>());
    }
    this.enemyMap.get(enemyName).add(
        new EnemyTuple(event.getBearing() + this.getHeading(), event.getDistance(), event
            .getEnergy(), this.getX(), this.getY()));
  }

  /**
   * Increments the number of misses when a fired bullet misses the enemy and hits a wall.
   * 
   * @param event The {@link BulletMissedEvent} containing information about the bullet hitting a
   * wall.
   */
  @Override
  public void onBulletMissed(BulletMissedEvent event) {
    this.numMisses++;
  }

  /**
   * Removes the enemy {@link Robot} from the stored information map when it dies.
   * 
   * @param event The {@link RobotDeathEvent} containing the dead Robot's information.
   */
  @Override
  public void onRobotDeath(RobotDeathEvent event) {
    this.enemyMap.remove(event.getName());
  }
} // End Orthogonobot
