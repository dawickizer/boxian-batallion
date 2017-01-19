import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Test;

public class QuadTreeNodeTest {

	@Test
	public void testSmallAdd() {
		Box[] boxes = {new Box(5, 10, 5, 10), 
					   new Box(6, 11, 6, 11), 
					   new Box(25, 30, 25, 30), 
					   new Box(26, 31, 26, 31)};
		
		QuadTreeNode<Sprite> root = new QuadTreeNode(new Box(0, 32, 0, 32), 0);
		
		ArrayList<Sprite> sprites = new ArrayList<>();
		
		for (Box box : boxes) {
			sprites.add(new Sprite(box));
		}
		
		for (Sprite sprite : sprites) {
			root.add(sprite);
		}

		assertEquals(null, root.southEast);
		assertEquals(null, root.southWest);
		assertEquals(null, root.northEast);
		assertEquals(null, root.northWest);
		assertEquals(4, root.sprites.size());

		for (Sprite sprite : sprites) {
			assertTrue(root.sprites.contains(sprite));
		}
	}
	

	@Test
	public void testMediumAdd() {
		Box[] boxes = {new Box(5, 10, 5, 10), // Should be in nw quadrant
					   new Box(6, 11, 6, 11), // Should be in nw quadrant
					   new Box(25, 30, 25, 30), // Should be in se quadrant
					   new Box(26, 31, 26, 31)}; // Should be in se quadrant
		
		QuadTreeNode<Sprite> root = new QuadTreeNode(new Box(0, 32, 0, 32), 0);
		
		ArrayList<Sprite> sprites = new ArrayList<>();
		
		for (int i = 0; i < 6; i++) {
			for (Box box : boxes) {
				sprites.add(new Sprite(box));
			}
		}
		
		for (Sprite sprite : sprites) {
			root.add(sprite);
		}

		assertNotEquals(null, root.southEast);
		assertNotEquals(null, root.southWest);
		assertNotEquals(null, root.northEast);
		assertNotEquals(null, root.northWest);
		assertTrue(null == root.sprites || root.sprites.isEmpty());

		assertTrue(root.northWest.sprites.contains(sprites.get(0)));
		assertTrue(root.northWest.sprites.contains(sprites.get(1)));
		assertTrue(root.southEast.sprites.contains(sprites.get(2)));
		assertTrue(root.southEast.sprites.contains(sprites.get(3)));

		assertFalse(root.northWest.sprites.contains(sprites.get(3)));
		assertFalse(root.northWest.sprites.contains(sprites.get(2)));
		assertFalse(root.southEast.sprites.contains(sprites.get(1)));
		assertFalse(root.southEast.sprites.contains(sprites.get(0)));
		
	}
	

	@Test
	public void testFindIntersecting() {
		Box[] boxes = {new Box(4, 10, 4, 10), 
					   new Box(6, 11, 6, 11), 
					   new Box(25, 30, 25, 30), 
					   new Box(26, 32, 26, 32)};
		
		QuadTreeNode<Sprite> root = new QuadTreeNode(new Box(0, 34, 0, 34), 0);
		
		ArrayList<Sprite> sprites = new ArrayList<>();
		
		for (Box box : boxes) {
			sprites.add(new Sprite(box));
		}
		
		for (int i = 0; i < 30; i++) {
			sprites.add(new Sprite(boxes[3]));
		}
		
		for (Sprite sprite : sprites) {
			root.add(sprite);
		}

		assertTrue(root.findIntersecting(new Box(3, 5, 3, 5)).contains(sprites.get(0)));
		assertEquals(1, root.findIntersecting(new Box(3, 5, 3, 5)).size());
		assertEquals(31, root.findIntersecting(new Box(31, 32, 31, 32)).size());
		assertEquals(0, root.findIntersecting(new Box(5, 6, 26, 30)).size());
	}


	@Test
	public void testAllQuadrants() {
		Box[] boxes = {new Box(5, 10, 25, 30), // Should be in sw quadrant
					   new Box(6, 11, 6, 11), // Should be in nw quadrant
					   new Box(25, 30, 25, 30), // Should be in se quadrant
					   new Box(26, 31, 2, 4)}; // Should be in ne quadrant
		
		QuadTreeNode<Sprite> root = new QuadTreeNode(new Box(0, 32, 0, 32), 0);
		
		ArrayList<Sprite> sprites = new ArrayList<>();
		
		for (int i = 0; i < 6; i++) {
			for (Box box : boxes) {
				sprites.add(new Sprite(box));
			}
		}
		
		for (Sprite sprite : sprites) {
			root.add(sprite);
		}

		assertNotEquals(null, root.southEast);
		assertNotEquals(null, root.southWest);
		assertNotEquals(null, root.northEast);
		assertNotEquals(null, root.northWest);
		assertTrue(null == root.sprites || root.sprites.isEmpty());

		for (int i = 0; i < 20; i+=4) {
			assertTrue(root.southWest.sprites.contains(sprites.get(i)));
			assertFalse(root.southWest.sprites.contains(sprites.get(i+1)));
			assertFalse(root.southWest.sprites.contains(sprites.get(i+2)));
			assertFalse(root.southWest.sprites.contains(sprites.get(i+3)));

			assertFalse(root.northWest.sprites.contains(sprites.get(i)));
			assertTrue(root.northWest.sprites.contains(sprites.get(i+1)));
			assertFalse(root.northWest.sprites.contains(sprites.get(i+2)));
			assertFalse(root.northWest.sprites.contains(sprites.get(i+3)));

			assertFalse(root.southEast.sprites.contains(sprites.get(i)));
			assertFalse(root.southEast.sprites.contains(sprites.get(i+1)));
			assertTrue(root.southEast.sprites.contains(sprites.get(i+2)));
			assertFalse(root.southEast.sprites.contains(sprites.get(i+3)));

			assertFalse(root.northEast.sprites.contains(sprites.get(i)));
			assertFalse(root.northEast.sprites.contains(sprites.get(i+1)));
			assertFalse(root.northEast.sprites.contains(sprites.get(i+2)));
			assertTrue(root.northEast.sprites.contains(sprites.get(i+3)));
		}
		
	}
	


	@Test
	public void testRemove() {
		Box[] boxes = {new Box(5, 10, 25, 30), // Should be in sw quadrant
					   new Box(6, 11, 6, 11), // Should be in nw quadrant
					   new Box(25, 30, 25, 30), // Should be in se quadrant
					   new Box(26, 31, 2, 4)}; // Should be in ne quadrant
		
		QuadTreeNode<Sprite> root = new QuadTreeNode(new Box(0, 32, 0, 32), 0);
		
		ArrayList<Sprite> sprites = new ArrayList<>();
		
		for (int i = 0; i < 6; i++) {
			for (Box box : boxes) {
				sprites.add(new Sprite(box));
			}
		}
		
		for (Sprite sprite : sprites) {
			root.add(sprite);
		}

		assertNotEquals(null, root.southEast);
		assertNotEquals(null, root.southWest);
		assertNotEquals(null, root.northEast);
		assertNotEquals(null, root.northWest);
		assertTrue(null == root.sprites || root.sprites.isEmpty());

		for (int i = 0; i < 20; i+=4) {
			assertTrue(root.southWest.sprites.contains(sprites.get(i)));
			assertFalse(root.southWest.sprites.contains(sprites.get(i+1)));
			assertFalse(root.southWest.sprites.contains(sprites.get(i+2)));
			assertFalse(root.southWest.sprites.contains(sprites.get(i+3)));

			assertFalse(root.northWest.sprites.contains(sprites.get(i)));
			assertTrue(root.northWest.sprites.contains(sprites.get(i+1)));
			assertFalse(root.northWest.sprites.contains(sprites.get(i+2)));
			assertFalse(root.northWest.sprites.contains(sprites.get(i+3)));

			assertFalse(root.southEast.sprites.contains(sprites.get(i)));
			assertFalse(root.southEast.sprites.contains(sprites.get(i+1)));
			assertTrue(root.southEast.sprites.contains(sprites.get(i+2)));
			assertFalse(root.southEast.sprites.contains(sprites.get(i+3)));

			assertFalse(root.northEast.sprites.contains(sprites.get(i)));
			assertFalse(root.northEast.sprites.contains(sprites.get(i+1)));
			assertFalse(root.northEast.sprites.contains(sprites.get(i+2)));
			assertTrue(root.northEast.sprites.contains(sprites.get(i+3)));
		}
		
		root.remove(sprites.get(0));
		root.remove(sprites.get(1));
		root.remove(sprites.get(2));
		root.remove(sprites.get(3));

		assertFalse(root.southWest.sprites.contains(sprites.get(0)));
		assertFalse(root.southWest.sprites.contains(sprites.get(1)));
		assertFalse(root.southWest.sprites.contains(sprites.get(2)));
		assertFalse(root.southWest.sprites.contains(sprites.get(3)));

		for (int i = 4; i < 20; i+=4) {
			assertTrue(root.southWest.sprites.contains(sprites.get(i)));
			assertFalse(root.southWest.sprites.contains(sprites.get(i+1)));
			assertFalse(root.southWest.sprites.contains(sprites.get(i+2)));
			assertFalse(root.southWest.sprites.contains(sprites.get(i+3)));

			assertFalse(root.northWest.sprites.contains(sprites.get(i)));
			assertTrue(root.northWest.sprites.contains(sprites.get(i+1)));
			assertFalse(root.northWest.sprites.contains(sprites.get(i+2)));
			assertFalse(root.northWest.sprites.contains(sprites.get(i+3)));

			assertFalse(root.southEast.sprites.contains(sprites.get(i)));
			assertFalse(root.southEast.sprites.contains(sprites.get(i+1)));
			assertTrue(root.southEast.sprites.contains(sprites.get(i+2)));
			assertFalse(root.southEast.sprites.contains(sprites.get(i+3)));

			assertFalse(root.northEast.sprites.contains(sprites.get(i)));
			assertFalse(root.northEast.sprites.contains(sprites.get(i+1)));
			assertFalse(root.northEast.sprites.contains(sprites.get(i+2)));
			assertTrue(root.northEast.sprites.contains(sprites.get(i+3)));
		}
	}

	@Test
	public void testAllItems() {
		Box[] boxes = {new Box(5, 10, 25, 30), // Should be in sw quadrant
					   new Box(6, 11, 6, 11), // Should be in nw quadrant
					   new Box(25, 30, 25, 30), // Should be in se quadrant
					   new Box(26, 31, 2, 4)}; // Should be in ne quadrant
		
		QuadTreeNode<Sprite> root = new QuadTreeNode(new Box(0, 32, 0, 32), 0);
		
		ArrayList<Sprite> sprites = new ArrayList<>();
		
		for (int i = 0; i < 6; i++) {
			for (Box box : boxes) {
				sprites.add(new Sprite(box));
			}
		}
		
		for (Sprite sprite : sprites) {
			root.add(sprite);
		}

		Set<Sprite> allItems = root.allItems();
		
		assertEquals(24, allItems.size());
		assertTrue(allItems.containsAll(sprites));
	}
	

	@Test
	public void testContains() {
		Box[] boxes = {new Box(5, 10, 25, 30), // Should be in sw quadrant
					   new Box(6, 11, 6, 11), // Should be in nw quadrant
					   new Box(25, 30, 25, 30), // Should be in se quadrant
					   new Box(26, 31, 2, 4)}; // Should be in ne quadrant
		
		QuadTreeNode<Sprite> root = new QuadTreeNode(new Box(0, 32, 0, 32), 0);
		
		ArrayList<Sprite> sprites = new ArrayList<>();
		
		for (int i = 0; i < 6; i++) {
			for (Box box : boxes) {
				sprites.add(new Sprite(box));
			}
		}
		
		for (Sprite sprite : sprites) {
			root.add(sprite);
		}

		for (Sprite sprite : sprites) {
			assertTrue(root.contains(sprite));
		}
		
		assertFalse(root.contains(new Sprite(new Box(3, 4, 3, 4))));
		assertFalse(root.contains(new Sprite(boxes[0])));
	}

	@Test
	public void testIsLeaf() {
		Box[] boxes = {new Box(5, 10, 25, 30), // Should be in sw quadrant
					   new Box(6, 11, 6, 11), // Should be in nw quadrant
					   new Box(25, 30, 25, 30), // Should be in se quadrant
					   new Box(26, 31, 2, 4)}; // Should be in ne quadrant
		
		QuadTreeNode<Sprite> root = new QuadTreeNode(new Box(0, 32, 0, 32), 0);
		
		ArrayList<Sprite> sprites = new ArrayList<>();
		
		for (int i = 0; i < 6; i++) {
			for (Box box : boxes) {
				sprites.add(new Sprite(box));
			}
		}
		
		for (Sprite sprite : sprites) {
			root.add(sprite);
		}

		assertFalse(root.isLeaf());
		assertTrue(root.southEast.isLeaf());
		assertTrue(root.southWest.isLeaf());
		assertTrue(root.northEast.isLeaf());
		assertTrue(root.northWest.isLeaf());
	}

	@Test
	public void testBoxRegion() {

		Box region = new Box(0, 32, 0, 32);
		QuadTreeNode<Sprite> root = new QuadTreeNode(region, 0);
		assertEquals(region, root.getBoxRegion());
	}

}
