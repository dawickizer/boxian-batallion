import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

public abstract class AbstractCollisionSetTest {

 public abstract CollisionSet<Sprite> newCollisionSet();
 public abstract ArrayList<Sprite> getItemsIn(CollisionSet<Sprite> spriteSet);
 
 @Test
 public void testAddAll() {
  CollisionSet<Sprite> spriteSet = newCollisionSet();
  
  Sprite sprite1 = new Sprite(new Box(0, 10, 0, 10));
  Sprite sprite2 = new Sprite(new Box(5, 15, 5, 15));
  Sprite sprite3 = new Sprite(new Box(17, 20, 10, 10));
  Sprite sprite4 = new Sprite(new Box(30, 35, 30, 35));
  
  ArrayList<Sprite> items = new ArrayList<Sprite>();
  Collections.addAll(items, sprite1, sprite2, sprite3, sprite4);
  spriteSet.addAll(items);
  ArrayList<Sprite> itemsInSpriteSet = getItemsIn(spriteSet);
  
  assertTrue(items.containsAll(itemsInSpriteSet));
  assertTrue(itemsInSpriteSet.containsAll(items));
 }
 @Test
 public void testAdd() {
  CollisionSet<Sprite> spriteSet = newCollisionSet();
  
  Sprite sprite1 = new Sprite(new Box(0, 10, 0, 10));
  Sprite sprite2 = new Sprite(new Box(5, 15, 5, 15));
  Sprite sprite3 = new Sprite(new Box(17, 20, 10, 10));
  Sprite sprite4 = new Sprite(new Box(30, 35, 30, 35));
  
  ArrayList<Sprite> items = new ArrayList<Sprite>();
  Collections.addAll(items, sprite1, sprite2, sprite3, sprite4);
  for (Sprite item : items) {
   spriteSet.add(item);
  }
  ArrayList<Sprite> itemsInSpriteSet = getItemsIn(spriteSet);
  
  assertTrue(items.containsAll(itemsInSpriteSet));
  assertTrue(itemsInSpriteSet.containsAll(items));
 }

 @Test
 public void testRemove() {
  CollisionSet<Sprite> spriteSet = newCollisionSet();
  
  Sprite sprite1 = new Sprite(new Box(0, 10, 0, 10));
  Sprite sprite2 = new Sprite(new Box(5, 15, 5, 15));
  Sprite sprite3 = new Sprite(new Box(17, 20, 10, 10));
  Sprite sprite4 = new Sprite(new Box(30, 35, 30, 35));
  
  ArrayList<Sprite> items = new ArrayList<Sprite>();
  Collections.addAll(items, sprite1, sprite2, sprite3, sprite4);
  spriteSet.addAll(items);
  
  items.remove(sprite1);
  spriteSet.remove(sprite1);
  
  ArrayList<Sprite> itemsInSpriteSet = getItemsIn(spriteSet);
  
  assertTrue(items.containsAll(itemsInSpriteSet));
  assertTrue(itemsInSpriteSet.containsAll(items));
 }

 @Test
 public void testClear() {
  CollisionSet<Sprite> spriteSet = newCollisionSet();
  
  Sprite sprite1 = new Sprite(new Box(0, 10, 0, 10));
  Sprite sprite2 = new Sprite(new Box(5, 15, 5, 15));
  Sprite sprite3 = new Sprite(new Box(17, 20, 10, 10));
  Sprite sprite4 = new Sprite(new Box(30, 35, 30, 35));
  
  ArrayList<Sprite> items = new ArrayList<Sprite>();
  Collections.addAll(items, sprite1, sprite2, sprite3, sprite4);
  spriteSet.addAll(items);
  spriteSet.clear();
  ArrayList<Sprite> itemsInSpriteSet = getItemsIn(spriteSet);
  
  assertEquals(0, itemsInSpriteSet.size());
 }

 @Test
 public void testFindIntersecting() {
  CollisionSet<Sprite> spriteSet = newCollisionSet();

  Sprite sprite1 = new Sprite(new Box(0, 10, 0, 10));
  Sprite sprite2 = new Sprite(new Box(5, 15, 5, 15));
  Sprite sprite3 = new Sprite(new Box(17, 20, 10, 10));
  Sprite sprite4 = new Sprite(new Box(30, 35, 30, 35));
  
  ArrayList<Sprite> items = new ArrayList<Sprite>();
  Collections.addAll(items, sprite1, sprite2, sprite3, sprite4);
  spriteSet.addAll(items);
  
  Set<Sprite> intSet = spriteSet.findIntersecting(new Box(3, 7, 3, 7));
  
  assertEquals(2, intSet.size());
  assertTrue(intSet.contains(sprite1));
  assertTrue(intSet.contains(sprite2));
 }

 @Test
 public void testContains() {
  CollisionSet<Sprite> spriteSet = newCollisionSet();
  
  Sprite sprite1 = new Sprite(new Box(0, 10, 0, 10));
  Sprite sprite2 = new Sprite(new Box(5, 15, 5, 15));
  Sprite sprite3 = new Sprite(new Box(17, 20, 10, 10));
  Sprite sprite4 = new Sprite(new Box(30, 35, 30, 35));
  
  ArrayList<Sprite> items = new ArrayList<Sprite>();
  Collections.addAll(items, sprite1, sprite2, sprite3, sprite4);
  spriteSet.addAll(items);

  assertTrue(spriteSet.contains(sprite1));
  assertTrue(spriteSet.contains(sprite2));
  assertTrue(spriteSet.contains(sprite3));
  assertTrue(spriteSet.contains(sprite4));
  assertTrue(!spriteSet.contains(new Sprite(new Box(0, 0, 0, 0))));
 }

 @Test
 public void testIterator() {
  CollisionSet<Sprite> spriteSet = newCollisionSet();
  
  Sprite sprite1 = new Sprite(new Box(0, 10, 0, 10));
  Sprite sprite2 = new Sprite(new Box(5, 15, 5, 15));
  Sprite sprite3 = new Sprite(new Box(17, 20, 10, 10));
  Sprite sprite4 = new Sprite(new Box(30, 35, 30, 35));
  
  ArrayList<Sprite> items = new ArrayList<Sprite>();
  Collections.addAll(items, sprite1, sprite2, sprite3, sprite4);
  spriteSet.addAll(items);
  
  for (Iterator<Sprite> iterator = spriteSet.iterator(); 
    iterator.hasNext(); ) {
   Sprite sprite = iterator.next();
   assertTrue(items.contains(sprite));
   items.remove(sprite);
  }
  assertEquals(0, items.size());
 }

 @Test
 public void testSize() {
  CollisionSet<Sprite> spriteSet = newCollisionSet();
  
  Sprite sprite1 = new Sprite(new Box(0, 10, 0, 10));
  Sprite sprite2 = new Sprite(new Box(5, 15, 5, 15));
  Sprite sprite3 = new Sprite(new Box(17, 20, 10, 10));
  Sprite sprite4 = new Sprite(new Box(30, 35, 30, 35));
  
  ArrayList<Sprite> items = new ArrayList<Sprite>();
  Collections.addAll(items, sprite1, sprite2, sprite3, sprite4);
  
  assertEquals(0, spriteSet.size());
  
  spriteSet.addAll(items);
  
  assertEquals(4, spriteSet.size());
 }
 
	@Test
	public void testIteratorRemove() {
		CollisionSet<Sprite> spriteSet = newCollisionSet();
		
		Sprite sprite1 = new Sprite(new Box(0, 10, 0, 10));
		Sprite sprite2 = new Sprite(new Box(5, 15, 5, 15));
		Sprite sprite3 = new Sprite(new Box(17, 20, 10, 10));
		Sprite sprite4 = new Sprite(new Box(30, 35, 30, 35));
		
		ArrayList<Sprite> items = new ArrayList<>();
		Collections.addAll(items, sprite1, sprite2, sprite3, sprite4);
		spriteSet.addAll(items);
		
		for (Iterator<Sprite> iterator = spriteSet.iterator(); 
			 iterator.hasNext(); ) {
			Sprite sprite = iterator.next();
			assertTrue(items.contains(sprite));
			items.remove(sprite);
			iterator.remove();
		}
		assertEquals(0, items.size());
		assertEquals(0, spriteSet.size());
	}
	
	@Test
	public void testIteratorDoubleRemove() {
		CollisionSet<Sprite> spriteSet = newCollisionSet();
		
		Sprite sprite1 = new Sprite(new Box(0, 10, 0, 10));
		Sprite sprite2 = new Sprite(new Box(5, 15, 5, 15));
		Sprite sprite3 = new Sprite(new Box(17, 20, 10, 10));
		Sprite sprite4 = new Sprite(new Box(30, 35, 30, 35));
		
		ArrayList<Sprite> items = new ArrayList<>();
		Collections.addAll(items, sprite1, sprite2, sprite3, sprite4);
		spriteSet.addAll(items);
		
		try {
			for (Iterator<Sprite> iterator = spriteSet.iterator(); 
				 iterator.hasNext(); ) {
				Sprite sprite = iterator.next();
				assertTrue(items.contains(sprite));
				items.remove(sprite);
				
				iterator.remove();
				iterator.remove();
			}
			assertEquals(0, items.size());
		} catch (IllegalStateException ise) {
			return;
		}
		
		fail("Double remove did not throw IllegalStateException");	
	}

	@Test
	public void testIteratorRemoveBeforeNext() {
		CollisionSet<Sprite> spriteSet = newCollisionSet();
		
		Sprite sprite1 = new Sprite(new Box(0, 10, 0, 10));
		Sprite sprite2 = new Sprite(new Box(5, 15, 5, 15));
		Sprite sprite3 = new Sprite(new Box(17, 20, 10, 10));
		Sprite sprite4 = new Sprite(new Box(30, 35, 30, 35));
		
		ArrayList<Sprite> items = new ArrayList<>();
		Collections.addAll(items, sprite1, sprite2, sprite3, sprite4);
		spriteSet.addAll(items);
			
		try {
			Iterator<Sprite> iterator = spriteSet.iterator(); 
			iterator.remove();
		} catch (IllegalStateException ise) {
			return;
		}
		
		fail("Removing before calling next did not throw IllegalStateException");	
	}
}
