package ctf;

import robocode.Condition;
import robocode.Robot;

/**
 * The {@link Condition} triggered when the {@link Robot} is too close to an edge of the
 * battlefield.
 * 
 * @author Christopher Foo
 * 
 */
public class NearEdgeCondition extends Condition {

  /**
   * The {@link EdgeCase} describing which edge of the battlefield triggered this
   * {@link NearEdgeCondition}.
   */
  private EdgeCase edgeCase = EdgeCase.NONE;

  /**
   * The {@link Robot} to check to determine whether this {@link NearEdgeCondition} should be
   * triggered.
   */
  private Robot robot;

  /**
   * The closest that the {@link Robot} should get to the edge of the battlefield.
   */
  private final double EDGE_DISTANCE;

  /**
   * The width of the battlefield.
   */
  private final double battlefieldWidth;

  /**
   * The height of the battlefield.
   */
  private final double battlefieldHeight;

  /**
   * The width of the {@link Robot} (from the center of the robot to the edge).
   */
  private final double robotWidth;

  /**
   * The height of the {@link Robot} (from the center of the robot to the edge).
   */
  private final double robotHeight;

  /**
   * Creates a new {@link NearEdgeCondition}.
   * 
   * @param name The name of the NearEdgeCondition that will be created.
   * @param robot The {@link Robot} that is being checked.
   * @param edgeDistance The closest the Robot can be to the edge of the battlefield.
   */
  public NearEdgeCondition(String name, Robot robot, double edgeDistance) {
    super(name);
    this.robot = robot;
    this.EDGE_DISTANCE = edgeDistance;
    this.battlefieldHeight = robot.getBattleFieldHeight();
    this.battlefieldWidth = robot.getBattleFieldWidth();
    this.robotHeight = robot.getHeight() / 2;
    this.robotWidth = robot.getWidth() / 2;
  }

  /**
   * Checks which {@link EdgeCase} should be triggered based upon the {@link Robot}'s current
   * location on the battlefield.
   * 
   * @param x The current X coordinate of the Robot.
   * @param y The current Y coordinate of the Robot.
   * @param width The width of the Robot.
   * @param height The height of the Robot.
   * @return The EdgeCase corresponding to which edge of the battlefield that the Robot is too close
   * too.
   */
  public EdgeCase checkNearEdge(double x, double y, double width, double height) {
    EdgeCase vertical = EdgeCase.NONE;
    EdgeCase horizontal = EdgeCase.NONE;
    EdgeCase edgeCase;
    if (x - this.robotWidth < this.EDGE_DISTANCE) {
      horizontal = EdgeCase.LEFT;
    }
    else if (x + this.robotWidth > this.battlefieldWidth - this.EDGE_DISTANCE) {
      horizontal = EdgeCase.RIGHT;
    }

    if (y - this.robotHeight < this.EDGE_DISTANCE) {
      vertical = EdgeCase.BOTTOM;
    }
    else if (y + this.robotHeight > this.battlefieldHeight - this.EDGE_DISTANCE) {
      vertical = EdgeCase.TOP;
    }

    if (vertical == EdgeCase.NONE && horizontal == EdgeCase.NONE) {
      edgeCase = EdgeCase.NONE;
    }

    if (vertical == EdgeCase.TOP) {
      if (horizontal == EdgeCase.LEFT) {
        edgeCase = EdgeCase.TOP_LEFT;
      }

      else if (horizontal == EdgeCase.RIGHT) {
        edgeCase = EdgeCase.TOP_RIGHT;
      }
      else {
        edgeCase = vertical;
      }
    }

    else if (vertical == EdgeCase.BOTTOM) {
      if (horizontal == EdgeCase.LEFT) {
        edgeCase = EdgeCase.BOTTOM_LEFT;
      }

      else if (horizontal == EdgeCase.RIGHT) {
        edgeCase = EdgeCase.BOTTOM_RIGHT;
      }
      else {
        edgeCase = vertical;
      }
    }

    else {
      edgeCase = horizontal;
    }
    return edgeCase;
  }

  /**
   * Tests if the {@link Robot} is too close to the edge of the battlefield.
   * 
   * @return If the {@link Robot} is too close to the edge of the battlefield.
   */
  @Override
  public boolean test() {
    this.edgeCase =
        this.checkNearEdge(this.robot.getX(), this.robot.getY(), this.robotWidth, this.robotHeight);
    return !this.edgeCase.equals(EdgeCase.NONE);
  }

  /**
   * Gets the {@link EdgeCase} of this {@link NearEdgeCondition}.
   * 
   * @return The EdgeCase of this NearEdgeCondition.
   */
  public EdgeCase getEdgeCase() {
    return this.edgeCase;
  }
}
