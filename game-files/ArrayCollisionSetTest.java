import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ArrayCollisionSetTest extends AbstractCollisionSetTest {

 @Override
 public CollisionSet<Sprite> newCollisionSet() {
  return new ArrayCollisionSet<Sprite>();
 }

 @Override
 public ArrayList<Sprite> getItemsIn(CollisionSet<Sprite> spriteSet) {
  @SuppressWarnings("unchecked")
  ArrayCollisionSet<Sprite> acSet = (ArrayCollisionSet<Sprite>) spriteSet;
  ArrayList<Sprite> items = new ArrayList<Sprite>();
  items.addAll(acSet.items);
  return items;
 }

}
