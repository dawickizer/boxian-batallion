
import java.awt.Color;
import java.awt.Graphics;


/**
 * Base sprite class. 
 * @author John C. Bowers bowersjc@jmu.edu
 */
public class Sprite implements BoxIntersectable {

  protected Box bbox;

  public Sprite(Box bbox) {
    this.bbox = bbox;
  }

  public void draw(Graphics g) {
    g.setColor(Color.red);
    g.drawRect((int) bbox.minx(), (int) bbox.miny(), (int) bbox.width(), (int) bbox.height());
  }

  public void update(double delta) {

  }

  @Override
  public boolean intersects(BoxIntersectable boxIntersectableObject) {
    return bbox.intersects(boxIntersectableObject.boundingBox());
  }

  @Override
  public Box boundingBox() {
    return bbox;
  }
}
