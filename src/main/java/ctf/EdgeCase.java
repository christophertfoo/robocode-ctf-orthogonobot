package ctf;

/**
 * The edge that triggered the {@link NearEdgeCondition}.
 * 
 * @author Christopher Foo
 * 
 */
public enum EdgeCase {

  /**
   * Not close to any edge.
   */
  NONE,

  /**
   * Close to the top (North) edge of the battlefield.
   */
  TOP,

  /**
   * Close to the bottom (South) edge of the battlefield.
   */
  BOTTOM,

  /**
   * Close to the right (East) edge of the battlefield.
   */
  RIGHT,

  /**
   * Close to the left (West) edge of the battlefield.
   */
  LEFT,

  /**
   * Close to the top-right (North East) corner of the battlefield.
   */
  TOP_RIGHT,

  /**
   * Close to the top-left (North West) corner of the battlefield.
   */
  TOP_LEFT,

  /**
   * Close to the bottom-right (South East) corner of the battlefield.
   */
  BOTTOM_RIGHT,

  /**
   * Close to the bottom-left (South West) corner of the battlefield.
   */
  BOTTOM_LEFT
}
