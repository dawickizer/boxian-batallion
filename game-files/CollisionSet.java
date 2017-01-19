
import java.util.List;
import java.util.Set;


/**
 * The base sprite collision set ADT. 
 * @author John C. Bowers bowersjc@jmu.edu
 */
public interface CollisionSet<E extends BoxIntersectable> extends Iterable<E> {

  /**
   * Builds a collision set on the set of sprites.
   * 
   * @param item The items to add.
   */
  public void addAll(List<E> items);

  /**
   * Adds a sprite to the set.
   * 
   * @param item The item to add. 
   */
  public void add(E item);

  /**
   * Removes an item from the item set.
   * 
   * @param item The item to remove.
   * @return True if the item was removed, false if the item wasn't in this set.
   */
  public boolean remove(E item);

  /**
   * Clears all items in the set.
   */
  public void clear();

  /**
   * @param box A bounding box for searching on.
   * @return A list of all items that intersect the box.
   */
  public Set<E> findIntersecting(Box box);

  /**
   * 
   * @param item The item to test for containment.
   * @return True if item is in the set, false otherwise.
   */
  public boolean contains(E item);

  /**
   * 
   * @return The number of items in this collision set.
   */
  public int size();
}
