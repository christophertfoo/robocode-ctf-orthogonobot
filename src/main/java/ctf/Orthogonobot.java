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

  private static final int EDGE_DISTANCE = 40;

  /**
   * The {@link HashMap} containing the information about the enemy {@link Robots}.
   */
  private Map<String, ArrayList<EnemyTuple>> enemyMap =
      new HashMap<String, ArrayList<EnemyTuple>>();

  /**
   * The current velocity of this {@link Orthogonobot}.
   */
  private double currentVelocity = 50;

  private BattleMode currentMode = BattleMode.NORMAL;

  private double bodyTurnAngle;

  private double radarTurn = 180;

  private boolean initialRadarTurn = true;

  private boolean leadTarget = true;

  private long numShotsFired = 0;
  private long numMisses = 0;

  private Point destination = new Point(0, 0);

  private MovementVector predictionVector;

  /**
   * 
   * @return The velocity that this Orthogonobot should use to move away from the enemy bullet.
   */
  private double determineVelocity() {
    double robotWidth = this.getWidth() / 2;
    double robotHeight = this.getHeight() / 2;
    double heading = this.getHeading();
    double currentX = this.getX();
    double currentY = this.getY();
    Point forwardDestination = Helpers.calculateLocation(heading, 100, currentX, currentY);
    double forwardX = forwardDestination.getX();
    double forwardY = forwardDestination.getY();
    if ((forwardX > robotWidth + EDGE_DISTANCE || forwardX < this.getBattleFieldWidth()
        - EDGE_DISTANCE - robotWidth)
        && (forwardY > robotHeight + EDGE_DISTANCE || forwardY < this.getBattleFieldHeight()
            - EDGE_DISTANCE - robotHeight)) {
      return 100;
    }
    else {
      return -100;
    }
  }

  /**
   * Runs this {@link Orthogonobot}'s battle strategy.
   */
  @Override
  public void run() {
    this.addCustomEvent(new NearEdgeCondition("nearEdge", this, EDGE_DISTANCE));
    this.setAllColors(Color.WHITE);
    this.setAdjustGunForRobotTurn(true);

    this.setTurnRadarRight(360);
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

      if (shortestTupleList == null) {
        continue;
      }

      switch (this.currentMode) {
      case NAVIGATION:
        if (Math.abs(this.getDistanceRemaining()) < 1) {
          this.currentMode = BattleMode.NORMAL;
          out.println("Exiting Navigation mode?");
          this.currentVelocity = Math.abs(this.currentVelocity);
        }
        break;

      // Stay somewhat perpendicular to the closest enemy
      case NORMAL:
        out.println("NORMAL MODE");

        double currentX = this.getX();
        double currentY = this.getY();
        if (shortestDeltaEnergy < 0) {
          out.println("MOVE!");
          this.bodyTurnAngle = 0;
          this.currentVelocity = this.determineVelocity();
          this.setAhead(this.currentVelocity);
          this.destination =
              Helpers
                  .calculateLocation(this.getHeading(), this.currentVelocity, currentX, currentY);
        }
        else {
          this.destination.setX(currentX);
          this.destination.setY(currentY);
          this.bodyTurnAngle =
              Utils.normalRelativeAngleDegrees(shortestBearing - this.getHeading() - 90);
        }

        break;
      case OFFENSIVE:
        // Close in for the kill!
        this.bodyTurnAngle = Utils.normalRelativeAngleDegrees(shortestBearing - this.getHeading());
        this.setAhead(Math.abs(this.currentVelocity));
        break;
      default:
        break;
      }

      double turnAngle;

      if (this.leadTarget && ((double) this.numMisses) / this.numShotsFired > 50) {
        this.leadTarget = false;
      }

      if (!this.leadTarget) {
        turnAngle = Utils.normalRelativeAngleDegrees(shortestBearing - this.getGunHeading());
        this.setTurnGunRight(turnAngle);
      }
      else {
        if (shortestTupleList.size() >= 2) {
          // TODO DETERMINE ANGLE
          turnAngle = 1;
          this.setTurnGunRight(turnAngle);
        }
      }

      this.setTurnRight(this.bodyTurnAngle);

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
      this.execute();

      if (shortestEnergy < 30 && this.getEnergy() > 40) {
        this.currentMode = BattleMode.OFFENSIVE;
      }
    }
  }

  @Override
  public void onCustomEvent(CustomEvent event) {
    Condition eventCondition = event.getCondition();

    if (eventCondition.getName().equals("nearEdge") && this.currentMode != BattleMode.NAVIGATION) {
      this.currentMode = BattleMode.NAVIGATION;
      out.println("Entering NAVIGATION mode.");
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
    this.currentVelocity *= -1;
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
            .getEnergy()));
  }

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
