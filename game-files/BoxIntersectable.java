
/**
 *
 * @author John C. Bowers <bowersjc@jmu.edu>
 */
public interface BoxIntersectable {
    /**
     * 
     * @param intersectableObject
     * @return True if this intersects intersectableObject
     */
    public boolean intersects(BoxIntersectable boxIntersectableObject);
    
    /**
     * @return The bounding box representing the bounds of this box
     */
    public Box boundingBox();
}
