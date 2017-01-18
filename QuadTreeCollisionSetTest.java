import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

public class QuadTreeCollisionSetTest extends AbstractCollisionSetTest {

 @Override
 public CollisionSet<Sprite> newCollisionSet() {
  return new QuadTreeCollisionSet<Sprite>();
 }

 @Override
 public ArrayList<Sprite> getItemsIn(CollisionSet<Sprite> spriteSet) {
  @SuppressWarnings("unchecked")
  QuadTreeCollisionSet<Sprite> qtSpriteSet = (QuadTreeCollisionSet<Sprite>) spriteSet;
  ArrayList<Sprite> returnList = new ArrayList<Sprite>();
  returnList.addAll(qtSpriteSet.root.allItems());
  return returnList;
 }

}
