
import java.awt.Color;
import java.awt.Graphics;


/**
 * The basic star sprite.
 * 
 * @author John C. Bowers bowersjc@jmu.edu
 */
public class StarSprite extends Sprite {

  protected double speed;
  protected boolean colliding;
  protected boolean active;

  protected static int STAR_SIZE = 2;

  /**
   * 
   * @param currentSpeed Base speed of the star.
   * @param worldWidth The world height.
   * @param worldHeight The world width.
   */
  public StarSprite(double currentSpeed, int worldWidth, int worldHeight) {
    super(new Box(0, 0, 0, 0));

    this.speed = (currentSpeed + (GamePanel.random.nextDouble())) * 0.35;

    int randY = GamePanel.random.nextInt(worldHeight - StarSprite.STAR_SIZE);

    this.bbox.xint.min = worldWidth;
    this.bbox.xint.max = worldWidth + StarSprite.STAR_SIZE;
    this.bbox.yint.min = randY;
    this.bbox.yint.max = randY + StarSprite.STAR_SIZE;

    this.colliding = false;
    this.active = true;
  }

  /**
   * Draw the star. 
   * @param g The graphics context to draw into.
   */
  public void draw(Graphics g) {
    if (this.colliding) {
      g.setColor(StarSprite.darkDarkGray);
    } else {
      g.setColor(Color.darkGray);
    }
    g.drawRect((int) bbox.minx(), (int) bbox.miny(), (int) bbox.width(), (int) bbox.height());
  }

  /**
   * Update the star's position.
   * @param delta The time delta since last update.
   */
  public void update(double delta) {
    this.bbox.xint.min -= delta * speed;
    this.bbox.xint.max -= delta * speed;
    if (this.bbox.xint.max <= 0) {
      active = false;
    }
  }

  private static Color darkDarkGray = new Color(32, 32, 32);
}
