package ctf;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import robocode.BattleResults;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.testing.RobotTestBed;

/**
 * Tests the {@link Orthogonobot} against the Crazy sample robot.
 * 
 * @author Christopher Foo
 */
public class TestOrthogonobotVersusCrazy extends RobotTestBed {

  /**
   * Specifies that Crazy and {@link Orthogonobot} are to be matched up in this test case.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.Crazy,ctf.Orthogonobot";
  }

  /**
   * This test runs for 10 rounds.
   * 
   * @return The number of rounds.
   */
  @Override
  public int getNumRounds() {
    return 100;
  }

  /**
   * The actual test, which asserts that {@link Orthogonobot} has won at least a quarter of the
   * rounds against Crazy.
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

    // Check to make sure Orthogonobot at least a quarter of the rounds.
    assertTrue("Check Orthogonobot winner", OrthogonobotResults.getFirsts() > getNumRounds() * 0.9);
  }
}
