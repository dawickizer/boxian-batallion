import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Represents and stores a set of Sprites that are intersectable, in array list form.
 *
 * @author David Wickizer wickizda@dukes.jmu.edu
 *
 * This work complies with the JMU Honor Code.
 * References and Acknowledgments: I received no outside help with this
 * programming assignment.
 * 
 * -David Wickizer
 */
public class ArrayCollisionSet<E extends BoxIntersectable> implements CollisionSet<E> {

  // Store the sprites in an array list
  ArrayList<E> items;

  public ArrayCollisionSet() {
    items = new ArrayList<E>();
  }

  @Override
  public void addAll(List<E> items) {
    this.items.addAll(items);
  }

  @Override
  public void add(E item) {
   items.add(item);
  }

  @Override
  public boolean remove(E item) {
    return items.remove(item);
  }

  @Override
  public void clear() {
    items.clear();
  }

  @Override
  public Set<E> findIntersecting(Box box) {
    
    Set<E> intersecting = new HashSet<E>();
    
    // Iterate through items and check if they are intersecting box..if so add them to intersecting
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i).boundingBox().intersects(box)) {
        intersecting.add(items.get(i));
      }
    }
    return intersecting;
  }

  @Override
  public boolean contains(E item) {
    return items.contains(item);
  }

  @Override
  public Iterator<E> iterator() {
    // HINT: Do this the easy way, no need to implement your own.
    return items.iterator();
  }

  @Override
  public int size() {
    return items.size();
  }
}
