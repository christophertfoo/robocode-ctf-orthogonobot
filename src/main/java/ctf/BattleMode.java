package ctf;

/**
 * The mode of the {@link Orthogonobot}.
 * 
 * @author Christopher Foo
 * 
 */
public enum BattleMode {
  /**
   * The standard mode of the {@link Orthogonobot} where it dodges bullets and shoots at the enemy.
   */
  NORMAL,

  /**
   * When the enemy is almost dead, the {@link Orthogonobot} closes in for highly accurate, high
   * power shots.
   */
  OFFENSIVE,

  /**
   * The {@link Orthogonobot} is too close to the edge of the battlefield and moves towards the
   * center of the battlefield.
   */
  NAVIGATION
}
