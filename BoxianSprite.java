import java.awt.Color;
import java.awt.Graphics;

/**
 * A boxian ship.
 * 
 * @author John C. Bowers bowersjc@jmu.edu
 */
public class BoxianSprite extends Sprite {

  protected static int SIZE_MIN = 12;
  protected static int SIZE_RANGE = 45;

  protected boolean active;
  protected double health;
  protected boolean isHit;
  protected boolean isOverlapping;

  protected double speed;

  /**
   * 
   * @param currentSpeed The base speed of this boxian.
   * @param worldWidth The width of the world.
   * @param worldHeight The height of the world.
   */
  public BoxianSprite(double currentSpeed, int worldWidth, int worldHeight) {
    super(new Box(0, 0, 0, 0));

    this.active = true;

    int width = GamePanel.random.nextInt(BoxianSprite.SIZE_RANGE) + BoxianSprite.SIZE_MIN;
    int height = GamePanel.random.nextInt(BoxianSprite.SIZE_RANGE) + BoxianSprite.SIZE_MIN;

    int randY = GamePanel.random.nextInt(worldHeight - height);

    this.bbox.xint.min = worldWidth;
    this.bbox.xint.max = worldWidth + width;
    this.bbox.yint.min = randY;
    this.bbox.yint.max = randY + height;

    this.health = width * height;
    this.speed = (currentSpeed + (GamePanel.random.nextDouble())) * 0.25;
    this.isHit = false;
  }

  /**
   * Decrements the health of this boxian. Call when the boxian is 
   * hit by a laser.
   */
  public void laserHit() {
    if (this.isOverlapping) {
      health -= 20;
    } else {
      health -= 100;
    }

    if (this.bbox.xint.length() > 10.0) {
      this.bbox.xint.min++;
      this.bbox.xint.max--;
    }

    if (this.bbox.yint.length() > 10.0) {
      this.bbox.yint.min++;
      this.bbox.yint.max--;
    }

    this.isHit = true;
  }

  /**
   * Update the position of this boxian based on the time
   * delta from last update. 
   * @param delta The time delta since last update. 
   */
  public void update(double delta) {
    if (health <= 0 || this.bbox.xint.max < 0) {
      this.active = false;
    } else {
      this.bbox.xint.min -= delta * speed;
      this.bbox.xint.max -= delta * speed;
      if (this.bbox.xint.max <= 0) {
        active = false;
      }
    }
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  /**
   * Draw this boxian to the graphics context. 
   * @param g The context to draw to. 
   */
  public void draw(Graphics g) {
    if (this.isHit) {
      this.isHit = false;
      super.draw(g);
    } else {

      if (this.isOverlapping) {
        g.setColor(Color.green);
      } else {
        g.setColor(Color.white);
      }
      g.drawRect((int) bbox.minx(), (int) bbox.miny(), (int) bbox.width(), (int) bbox.height());
    }
  }
}
