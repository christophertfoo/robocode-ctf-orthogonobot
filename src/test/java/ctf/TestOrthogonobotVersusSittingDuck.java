package ctf;

import static org.junit.Assert.assertEquals;
import robocode.BattleResults;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.testing.RobotTestBed;

/**
 * Tests the {@link Orthogonobot} against the SittingDuck sample robot.
 * 
 * @author Christopher Foo
 */
public class TestOrthogonobotVersusSittingDuck extends RobotTestBed {

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
   * This test runs for 10 rounds.
   * 
   * @return The number of rounds.
   */
  @Override
  public int getNumRounds() {
    return 10;
  }

  /**
   * The actual test, which asserts that {@link Orthogonobot} has won every round against
   * SittingDuck.
   * 
   * @param event Details about the completed battle.
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    // Return the results in order of getRobotNames.
    BattleResults[] battleResults = event.getIndexedResults();
    // Sanity check that results[0] is Orthogonobot.
    BattleResults OrthogonobotResults = battleResults[0];
    String robotName = OrthogonobotResults.getTeamLeaderName();
    assertEquals("Check that results[1] is Orthogonobot", "ctf.Orthogonobot*", robotName);

    // Check to make sure Orthogonobot won every round.
    assertEquals("Check Orthogonobot winner", getNumRounds(), OrthogonobotResults.getFirsts());
  }
}
