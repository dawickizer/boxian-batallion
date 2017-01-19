import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;

/**
 * Represents and stores a set of Sprites that are intersectable, in quad tree form.
 *
 * @author David Wickizer wickizda@dukes.jmu.edu
 *
 * This work complies with the JMU Honor Code.
 * References and Acknowledgments: I received no outside help with this
 * programming assignment.
 * 
 * -David Wickizer
 */
public class QuadTreeCollisionSet<E extends Sprite> implements CollisionSet<E> {

  protected QuadTreeNode<E> root;

  public QuadTreeCollisionSet() {
    root = new QuadTreeNode<>(new Box(Integer.MIN_VALUE, Integer.MAX_VALUE, 
                                      Integer.MIN_VALUE, Integer.MAX_VALUE), 0); 
  }
  
  public QuadTreeCollisionSet(JPanel panel) {
    root = new QuadTreeNode<>(new Box(0, panel.getWidth(),
                                      0, panel.getHeight()), 0); 
  }
  
  @Override
  public void addAll(List<E> sprites) {
    for (E sprite : sprites) {
      root.add(sprite);
    }
  }

  @Override
  public void add(E sprite) {
    root.add(sprite);
  }

  @Override
  public boolean remove(E sprite) {
    return root.remove(sprite);
  }

  @Override
  public void clear() {
    root.clear(); 
  }

  @Override
  public Set<E> findIntersecting(Box box) {
    return root.findIntersecting(box);
  }

  @Override
  public Iterator<E> iterator() {
    return new QTreeSpriteIterator();
  }

  @Override
  public int size() {
    return root.allItems().size();
  }

  @Override
  public boolean contains(E sprite) {
    return root.contains(sprite);
  }

  /**
   * Hint: You will need to implement this iterator to do the remove. Suggestion: use the allItems()
   * method of the QuadTreeNode to create a java Collections object of all items and use its
   * iterator. (Store it as theIterator).
   *
   * <p>
   * The tricky part is how to handle remove. Calling remove on theIterator only removes it from the
   * Collection iterator you made, but does not actually remove it from the tree.
   * </p>
   *
   * <p>
   * To handle remove, then, you'll need to store the last item you returned each time next() is
   * called, so that when remove() is called you can use the QuadTreeNode.remove(lastItem) method to
   * actually remove the item from the QuadTree.
   * </p>
   */
  private class QTreeSpriteIterator implements Iterator<E> {

    Iterator<E> theIterator;
    E lastItem = null;
    boolean nextCalled = false;
    
    public QTreeSpriteIterator() {
      this.theIterator = root.allItems().iterator();
    }

    @Override
    public boolean hasNext() {
      return theIterator.hasNext();
    }

    @Override
    public E next() {
      nextCalled = true;
      lastItem = theIterator.next();
      return lastItem;
    }

    @Override
    public void remove() {
      if (nextCalled) {
        root.remove(lastItem);
        nextCalled = false;
      } else {
        throw new IllegalStateException();
      }
    }
  }
}
