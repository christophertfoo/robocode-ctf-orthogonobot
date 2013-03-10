package ctf;

import static org.junit.Assert.fail;
import robocode.control.BattlefieldSpecification;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.testing.RobotTestBed;

/**
 * Tests that the {@link Orthogonobot} does not run into walls.
 * 
 * @author Christopher Foo
 */
public class TestWallAvoidance extends RobotTestBed {

  private static final double ACCEPTABLE_DISTANCE = 1;
  private static final double ROBOT_WIDTH = 18;
  private static final double ROBOT_HEIGHT = 18;

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
   * Checks if the {@link Orthogonobot} is too close to an edge and fails the test if it is too
   * close.
   * 
   * @param event Info about the current state of the battle.
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {

    // Give the robot 10 turns to move away from the starting position if it is put
    // too close to the edge of the battlefield.
    if (event.getTurnSnapshot().getTurn() < 10) {
      return;
    }
    BattlefieldSpecification bfSpec = this.battleFieldSpec;
    for (IRobotSnapshot snapshot : event.getTurnSnapshot().getRobots()) {
      if (snapshot.getName().contains("Orthogonobot")) {
        int battlefieldHeight = bfSpec.getHeight();
        int battlefieldWidth = bfSpec.getWidth();
        double x = snapshot.getX();
        double y = snapshot.getY();

        // If the robot is too close to an edge.
        if (x - ROBOT_WIDTH <= ACCEPTABLE_DISTANCE || y - ROBOT_HEIGHT <= ACCEPTABLE_DISTANCE
            || x + ROBOT_WIDTH >= battlefieldWidth - ACCEPTABLE_DISTANCE
            || y + ROBOT_HEIGHT >= battlefieldHeight - ACCEPTABLE_DISTANCE) {
          fail("Orthogonobot got too close to the edge of the battlefield. X = " + x + ", Y = " + y
              + ", Battlefield Width = " + battlefieldWidth + ", Battlefield Height = "
              + battlefieldHeight);
        }
      }
    }
  }
}