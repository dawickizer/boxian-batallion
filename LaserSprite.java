
/**
 * The Sprite for a laser shot.
 * 
 * @author John C. Bowers bowersjc@jmu.edu
 */
public class LaserSprite extends Sprite {

  protected boolean active = true;
  protected int worldWidth;
  protected int worldHeight;
  protected Direction direction;

  public enum Direction {
    UP, HORIZ, DOWN
  }

  /**
   * 
   * @param xcoord The x coordinate of this laser. 
   * @param ycoord The y coordinate of this laser. 
   * @param direction The direction this laser travels. 
   * @param worldWidth The width of the world. 
   * @param worldHeight The height of the world. 
   */
  public LaserSprite(double xcoord, 
                     double ycoord, 
                     Direction direction, 
                     int worldWidth, 
                     int worldHeight) {
    super(new Box(xcoord, xcoord + 2, ycoord - 1, ycoord + 1));
    this.worldWidth = worldWidth;
    this.worldHeight = worldHeight;
    this.direction = direction;
  }

  public boolean isActive() {
    return active;
  }

  /**
   * Update the laser's position .
   * @param delta The update time delta. 
   */
  public void update(double delta) {
    this.bbox.xint.min += 5 * delta;
    this.bbox.xint.max += 5 * delta;

    double ychange;
    switch (direction) {
      case UP:
        ychange = 5 * -delta;
        break;
      case DOWN:
        ychange = 5 * delta;
        break;
      case HORIZ:
      default:
        ychange = 0;
        break;
    }

    if (ychange != 0) {
      this.bbox.yint.min += ychange;
      this.bbox.yint.max += ychange;
    }

    if (this.bbox.xint.min > worldWidth || this.bbox.yint.max < 0
        || this.bbox.yint.min > worldHeight) {
      this.active = false;
    }
  }

}
