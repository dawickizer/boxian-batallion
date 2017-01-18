
/**
 *
 * @author John C. Bowers bowersjc@jmu.edu
 */
public class PlayerSprite extends Sprite {

  protected boolean upThrusters = false; // Are the up thrusters activated
  protected boolean downThrusters = false; // Are the down thrusters activated
  protected int worldHeight;

  
  /**
   * 
   * @param xcoord The x-coordinate of the player.
   * @param ycoord The y-coordinate of the player
   * @param worldHeight The height of the world.
   */
  public PlayerSprite(int xcoord, int ycoord, int worldHeight) {
    super(new Box(xcoord, xcoord + 20, ycoord - 4, ycoord + 4));
    this.worldHeight = worldHeight;
  }

  /**
   * @param delta The time delta since last update. 
   */
  public void update(double delta) {
    if (downThrusters) {
      double newymax = this.bbox.yint.max + delta;
      System.out.println(newymax);
      System.out.println(worldHeight);
      if (newymax < worldHeight) {
        this.bbox.yint.min += delta;
        this.bbox.yint.max = newymax;
      }
    }
    if (upThrusters) {
      double newymin = this.bbox.yint.min - delta;
      if (newymin > 0) {
        this.bbox.yint.min = newymin;
        this.bbox.yint.max -= delta;
      }
    }
  }

  /**
   * @return The laser cannon's projectile spawn x-coordinate.
   */
  public double getLaserX() {
    return this.bbox.maxx() + 5;
  }

  /**
   * @return The laser cannon's projectile spawn x-coordinate.
   */
  public double getLaserY() {
    return this.bbox.miny() + 4;
  }
  
  /**
   * Starts upward thrusters.
   */
  public void upThrustersOn() {
    upThrusters = true;
  }

  /**
   * Turns off the upward thrusters.
   */
  public void upThrustersOff() {
    upThrusters = false;
  }

  /**
   * Start downward thrusters.
   */
  public void downThrustersOn() {
    downThrusters = true;
  }

  /**
   * Stop downward thrusters.
   */
  public void downThrustersOff() {
    downThrusters = false;
  }

}
