package ctf;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IBulletSnapshot;
import robocode.control.testing.RobotTestBed;
import robocode.util.Utils;

/**
 * This tests for variability in bullet power administered by the robot {@link Orthogonobot}.
 * 
 * @author Christopher Foo
 */
public class TestOrthogonobotFiring extends RobotTestBed {

  /**
   * If the {@link Orthogonobot} fired a bullet with a power of 3.
   */
  private boolean firePower3 = false;
  
  /**
   * If the {@link Orthogonobot} fired a bullet with a power of 2.5.
   */
  private boolean firePower2Half = false;
  
  /**
   * If the {@link Orthogonobot} fired a bullet with a power of 2.
   */
  private boolean firePower2 = false;
  
  /**
   * If the {@link Orthogonobot} fired a bullet with a power of 1.
   */
  private boolean firePower1 = false;

  /**
   * Specifies that SittingDuck and {@link Orthogonobot} are to be matched up in this test case.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.SittingDuck,ctf.Orthogonobot";
  }

  /**
   * This test runs for 20 rounds.
   * 
   * @return The number of rounds.
   */
  @Override
  public int getNumRounds() {
    return 20;
  }

  /**
   * At the end of each turn, checks the power of all bullets moving across the battlefield. Checks
   * that the bullets are of a valid power level.
   * 
   * @param event Info about the current state of the battle.
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {

    // All active bullets belong to Orthogonobot since SittingDuck does not fire.
    IBulletSnapshot bullets[] = event.getTurnSnapshot().getBullets();
    double bulletPower;

    for (int i = 0; i < bullets.length; i++) {

      bulletPower = bullets[i].getPower();

      if (Utils.isNear(3, bulletPower)) {
        this.firePower3 = true;
      }
      else if (Utils.isNear(2.5, bulletPower)) {
        this.firePower2Half = true;
      }
      else if (Utils.isNear(2, bulletPower)) {
        this.firePower2 = true;
      }
      else if (Utils.isNear(1, bulletPower)) {
        this.firePower1 = true;
      }
      else {
        fail("Should not fire bullet with power of " + bulletPower);
      }
    }
  }

  /**
   * After running all matches, determine if {@link Orthogonobot} has had variability in its bullet
   * power.
   * 
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    assertTrue("Should be able to fire bullet with a power of 3", this.firePower3);
    assertTrue("Should be able to fire bullet with a power of 2.5", this.firePower2Half);
    assertTrue("Should be able to fire bullet with a power of 2", this.firePower2);
    assertTrue("Should be able to fire bullet with a power of 1", this.firePower1);
  }
}