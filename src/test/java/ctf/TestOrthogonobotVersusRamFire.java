package ctf;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import robocode.BattleResults;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.testing.RobotTestBed;

/**
 * Tests the {@link Orthogonobot} against the RamFire sample robot.
 * 
 * @author Christopher Foo
 */
public class TestOrthogonobotVersusRamFire extends RobotTestBed {

  /**
   * Specifies that RamFire and {@link Orthogonobot} are to be matched up in this test case.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.RamFire,ctf.Orthogonobot";
  }

  /**
   * This test runs for 50 rounds.
   * 
   * @return The number of rounds.
   */
  @Override
  public int getNumRounds() {
    return 50;
  }

  /**
   * The actual test, which asserts that {@link Orthogonobot} has won at least 90% of the rounds
   * against RamFire.
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
    assertEquals("Check that results[0] is Orthogonobot", "ctf.Orthogonobot*", robotName);

    // Check to make sure Orthogonobot won at least 90% of the rounds.
    assertTrue("Check Orthogonobot winner", OrthogonobotResults.getFirsts() > getNumRounds() * 0.9);
  }
}
