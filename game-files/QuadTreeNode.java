
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a quad tree node. If the node is a leaf then it contains a region and a set of
 * Sprites. If the node is an internal node then it has children nodes of NW, NE, SW, SE
 * (broken down equally into quadrants) which contain the Sprites intersecting with them.
 *
 * @author David Wickizer wickizda@dukes.jmu.edu
 *
 * This work complies with the JMU Honor Code.
 * References and Acknowledgments: I received no outside help with this
 * programming assignment.
 * 
 * -David Wickizer
 */
public class QuadTreeNode<E extends BoxIntersectable> {

  protected Box region;

  protected QuadTreeNode<E> northEast;
  protected QuadTreeNode<E> northWest;
  protected QuadTreeNode<E> southEast;
  protected QuadTreeNode<E> southWest;

  protected int depth;

  public static final int MAX_SPRITES = 20;
  public static final int MAX_DEPTH = 5;

  // The sprites stored
  HashSet<E> sprites;

  public QuadTreeNode(Box bbox, int depth) {
    sprites = new HashSet<E>();
    northEast = northWest = southEast = southWest = null;
    this.region = bbox;
    this.depth = depth;
  }

  /**
   * Add the item to this quadtree node.
   * 
   * <p>
   * If this node is an internal node, recursively add the item to any children of this node whose
   * regions intersect the item's box.
   * </p>
   * 
   * <p>
   * If this is a leaf node, store the item in a set that is contained within the node. If the
   * number of items stored within the node exceeds some threshold (e.g. MAX_SPRITES in
   * QuadTreeSpriteNode), but is not at some maximum depth (e.g. MAX_DEPTH in QuadTreeSpriteNode),
   * then split this node by dividing its region evenly among four new child nodes and adding all of
   * its nodes to the children.
   * </p>
   * 
   * @param item The item to add. 
   */
  public void add(E item) {
    if (isLeaf()) {

      // Store the item in the set that is contained within this node
      sprites.add(item);

      // Split node by dividing region evenly among 4 new child nodes
      if (sprites.size() > MAX_SPRITES && depth < MAX_DEPTH) {
        this.splitNode();
       
        // Add appropriate Sprites to appropriate children
        Iterator<E> iter = sprites.iterator();
        while (iter.hasNext()) {
          E sprite = iter.next();
          Box spriteBox = sprite.boundingBox();
          
          if (northWest.getBoxRegion().intersects(spriteBox)) {
            northWest.add(sprite);
          }
          if (northEast.getBoxRegion().intersects(spriteBox)) {
            northEast.add(sprite);
          }
          if (southEast.getBoxRegion().intersects(spriteBox)) {
            southEast.add(sprite);
          }
          if (southWest.getBoxRegion().intersects(spriteBox)) {
            southWest.add(sprite);
          }
        }
       
        // Clear the Sprites from the root
        sprites = null; 
      }
    } else {
      if (northWest.getBoxRegion().intersects(item.boundingBox())) {
        northWest.add(item);
      }
      if (northEast.getBoxRegion().intersects(item.boundingBox())) {
        northEast.add(item);
      }
      if (southEast.getBoxRegion().intersects(item.boundingBox())) {
        southEast.add(item);
      }
      if (southWest.getBoxRegion().intersects(item.boundingBox())) {
        southWest.add(item);
      }
    }
  }
  
  /**
   * Find all items intersecting a given bounding box.
   * 
   * <p>
   * Hints: Use a recursive algorithm with a helper. Create a HashSet to store the items and pass it
   * to the recursive calls.
   * </p>
   * 
   * <p>
   * Recurse on any child node whose region intersects the box. At a leaf node test each item stored
   * in the node to see if it intersects the box. If it does, add it to the HashSet that is
   * returned.
   * </p>
   * @param box The box to intersect objects with.
   * @return A set containing all of the items.
   */
  public Set<E> findIntersecting(Box box) {
    HashSet<E> set = new HashSet<E>();
    return findIntersecting(box, set);
  }
  
  /**
   * Helper method for findIntersecting().
   * 
   * @param box The box to intersect objects with.
   * @param set The set of intersected Sprites.
   * @return
   */
  private Set<E> findIntersecting(Box box, HashSet<E> set) {
    if (isLeaf()) {

      // Iterate over each sprite at this node to see if it intersects with box
      Iterator<E> iter = sprites.iterator();
      while (iter.hasNext()) {
        E sprite = iter.next();
 
        // Add sprite to set if it intersects with box
        if (box.intersects(sprite.boundingBox())) {
          set.add(sprite);
        }
      }
      return set;
    }
    
    // Recurse on any child node whose region intersects the box
    if (northWest.getBoxRegion().intersects(box)) {
      northWest.findIntersecting(box, set);
    }
    if (northEast.getBoxRegion().intersects(box)) {
      northEast.findIntersecting(box, set);
    }
    if (southEast.getBoxRegion().intersects(box)) {
      southEast.findIntersecting(box, set);
    }
    if (southWest.getBoxRegion().intersects(box)) {
      southWest.findIntersecting(box, set);
    }    
    return set;
  }

  /**
   * Remove the item from this quadtree
   * 
   * <p>
   * Hints: Similar to find. Recursively find all leaf nodes that intersect the item's bounding box
   * and remove the item from the leaf node, if it exists.
   * </p>
   * 
   * @param item The item to remove.
   * @return
   */
  public boolean remove(E sprite) {
    if (isLeaf()) {
      return sprites.remove(sprite);
    }

    boolean result = false;

    // Recurse on any child node whose region intersects the Sprite's bounding box
    // Only recurse if not found/removed yet 
    if (northWest.getBoxRegion().intersects(sprite.boundingBox()) && !result) {
      result = northWest.remove(sprite);
    }
    if (northEast.getBoxRegion().intersects(sprite.boundingBox()) && !result) {
      result = northEast.remove(sprite);
    }
    if (southEast.getBoxRegion().intersects(sprite.boundingBox()) && !result) {
      result = southEast.remove(sprite);
    }
    if (southWest.getBoxRegion().intersects(sprite.boundingBox()) && !result) {
      result = southWest.remove(sprite);
    }    
    return result;
  }

  /**
   * Return a set containing all of the items.
   * 
   * <p>
   * Hints: Use a recursive strategy. Create a set and pass it to a helper. The helper will recurse
   * on all children to get down to leaf nodes. At a leaf node call addAll on the HashSet to add all
   * items stored in that leaf.
   * </p>
   * @return A set containing all of the items in this quadtree.
   */
  public Set<E> allItems() {
    return allItems(new HashSet<E>());
  }
  
  /**
   * 
   * @param set The set to be updated and returned
   * @return The set of all items
   */
  private Set<E> allItems(Set<E> set) {
    if (isLeaf()) {
      set.addAll(sprites);
      return set;
    }
    
    // Recurse on children
    northWest.allItems(set);
    northEast.allItems(set);
    southEast.allItems(set);
    southWest.allItems(set);
    return set;
  }

  /**
   * Splits a node into 4 equal quadrents (NW, NE, SW, SE).
   */
  private void splitNode() {

    // Create new regions with Box Objects
    Box nw = new Box(region.minx(), region.xint.mid(), 
                     region.miny(), region.yint.mid());
    Box ne = new Box(region.xint.mid(), region.maxx(), 
                     region.miny(), region.yint.mid());
    Box sw = new Box(region.minx(), region.xint.mid(), 
                     region.yint.mid(), region.maxy());
    Box se = new Box(region.xint.mid(), region.maxx(), 
                     region.yint.mid(), region.maxy());
      
    // Create child nodes
    northWest = new QuadTreeNode<E>(nw, depth + 1);
    northEast = new QuadTreeNode<E>(ne, depth + 1);
    southWest = new QuadTreeNode<E>(sw, depth + 1);
    southEast = new QuadTreeNode<E>(se, depth + 1);
    
  }

  /**
   * Determine if this node is a leaf or not.
   * 
   * @return True if this is a leaf, false otherwise.
   */
  public boolean isLeaf() {
    return (northWest == null && northEast == null && southWest == null 
            && southEast == null);
  }

  /**
   * Hints:
   * 
   * <p>
   * Recursively test if the item is contained in the node. Similar to find.
   * </p>
   * 
   * @param item The item to test for. 
   * @return True if this tree contains this item.
   */
  public boolean contains(E sprite) {
    if (isLeaf()) {
      return sprites.contains(sprite);
    }
    
    boolean result = false;
    
    // Recurse on any child node whose region intersects the Sprite's bounding box
    // Only recurse if not found yet 
    if (northWest.getBoxRegion().intersects(sprite.boundingBox()) && !result) {
      result = northWest.contains(sprite);
    }
    if (northEast.getBoxRegion().intersects(sprite.boundingBox()) && !result) {
      result = northEast.contains(sprite);
    }
    if (southEast.getBoxRegion().intersects(sprite.boundingBox()) && !result) {
      result = southEast.contains(sprite);
    }
    if (southWest.getBoxRegion().intersects(sprite.boundingBox()) && !result) {
     result = southWest.contains(sprite);
    }    
    return result;
  }

  /**
   * 
   * @return The box representing the region this quadtree node covers.
   */
  public Box getBoxRegion() {
    return region;
  }
  
  /**
   * Clears the quad tree.
   */
  public void clear() {
    if (isLeaf()) {
      sprites.clear();
    } else {
      northWest.clear();
      northEast.clear();
      southWest.clear();
      southEast.clear();
    }
  }
}
