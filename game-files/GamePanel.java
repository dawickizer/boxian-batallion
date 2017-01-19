import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author John C. Bowers bowersjc@jmu.edu
 */
public class GamePanel extends JPanel {

  CollisionSet<BoxianSprite> boxianSet; // The set of boxians
  CollisionSet<StarSprite> starSet; // The set of stars
  PlayerSprite player; // The player
  LinkedList<LaserSprite> laserBeams; // All live laser beams
  double currentSpeed; // The current base speed of new boxians
  long nextBoxianAt; // The time at which to add the next boxian
  long nextStarAt; // The time at which to add the next star
  int boxianCountSinceLastSpeedup; // Number of boxians added since speed increase
  long rechargeMillis; // No. ms to next laser recharge
  int numShots; // Number of shots left before reload
  int fps; // Last frames-per-second computation

  /**
   * TODO Change this to return a QuadTreeSpriteCollisionSet when you've completed that step.
   * 
   * @return A BoxianSprite collision set.
   */
  public CollisionSet<BoxianSprite> newBoxianSet() {
    //return new ArrayCollisionSet<BoxianSprite>();
    return new QuadTreeCollisionSet<BoxianSprite>(this);
  }

  /**
   * TODO Change this to return a QuadTreeSpriteCollisionSet when you've completed that step.
   * 
   * @return A StarSprite collision set
   */
  public CollisionSet<StarSprite> newStarSet() {
    //return new ArrayCollisionSet<StarSprite>();
    return new QuadTreeCollisionSet<StarSprite>(this);
  }

  /**
   * Create a new game panel with a black background. Call initGame() once
   * the graphics have packed.
   */
  public GamePanel() {
    this.setBackground(Color.black);
    setFocusable(true);

    registerKeyListeners();
  }

  /**
   * Set up the initial game (also used to reset the game).
   */
  public void initGame() {

    boxianSet = newBoxianSet();
    starSet = newStarSet();

    player = new PlayerSprite(50, 50, this.getHeight());
    laserBeams = new LinkedList<LaserSprite>();
    currentSpeed = 1.0;
    nextBoxianAt = System.currentTimeMillis();
    boxianCountSinceLastSpeedup = 0;
    numShots = RELOAD_AMMO_COUNT;
    rechargeMillis = 0;

    state = GameState.GAME_ON;
  }

  /**
   * Start the game loop.
   */
  public void startGameLoop() {
    gameThread = new Thread() {
      @Override
      public void run() {
        lastLoopTime = System.nanoTime();
        gameLoop();
      }
    };
    gameThread.start();
  }

  /**
   * The main game loop.
   */
  private void gameLoop() {
    while (gameRunning) {
      try {
        long now = System.currentTimeMillis();
        long updateLength = now - lastLoopTime;
        lastLoopTime = now;
        double delta = updateLength / 10.0;
        fps = (int) (1.0 / (updateLength / 1000.0));

        updateGame(delta);
        this.paintImmediately(0, 0, this.getWidth(), this.getHeight());
        Thread.sleep(1);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Update all of the sprites.
   * 
   * @param delta The time delta since last update.
   */
  private void updateGame(double delta) {

    final long currentTime = System.currentTimeMillis();

    // Update all the boxian locations
    CollisionSet<BoxianSprite> newBoxianSet = newBoxianSet();
    for (BoxianSprite boxian : boxianSet) {
      boxian.update(delta);
      if (boxian.active) {
        newBoxianSet.add(boxian);
      }
    }
    boxianSet = newBoxianSet;

    // Update all the star locations
    CollisionSet<StarSprite> newStarSet = newStarSet();
    for (StarSprite star : starSet) {
      star.update(delta);
      star.colliding = false; // Reset this, we'll set it later
      if (star.active) {
        newStarSet.add(star);
      }
    }
    starSet = newStarSet;

    player.update(delta);

    for (Iterator<LaserSprite> it = laserBeams.iterator(); it.hasNext();) {

      LaserSprite laserBeam = it.next();
      laserBeam.update(delta);

      // Remove the laser if it's dead
      if (!laserBeam.isActive()) {
        it.remove();
      }
    }

    // If the spawnLaser bit is set, spawn a new laser
    if (spawnLaser && numShots > 0 && currentTime > rechargeMillis && (laserBeams.size() == 0
        || laserBeams.getLast().bbox.xint.min > player.getLaserX() + 2)) {

      LaserSprite newLaserBeam = new LaserSprite(player.getLaserX(), player.getLaserY(),
          LaserSprite.Direction.UP, this.getWidth(), this.getHeight());
      laserBeams.add(newLaserBeam);

      newLaserBeam = new LaserSprite(player.getLaserX(), player.getLaserY(),
          LaserSprite.Direction.DOWN, this.getWidth(), this.getHeight());
      laserBeams.add(newLaserBeam);

      newLaserBeam = new LaserSprite(player.getLaserX(), player.getLaserY(),
          LaserSprite.Direction.HORIZ, this.getWidth(), this.getHeight());
      laserBeams.add(newLaserBeam);

      numShots--;
    } else if (numShots <= 0) {
      numShots = RELOAD_AMMO_COUNT;
      rechargeMillis = currentTime + 5000;
    }

    // If we have passed the next boxian time, spawn a new boxian.
    if (currentTime > nextBoxianAt) {
      BoxianSprite newBoxian =
          new BoxianSprite(this.currentSpeed, this.getWidth(), this.getHeight());
      boxianSet.add(newBoxian);

      nextBoxianAt = System.currentTimeMillis() + BOXIAN_SPACING;
      boxianCountSinceLastSpeedup++;
    }

    // If we have passed the next star time, spawn a few new stars
    if (currentTime > nextStarAt) {
      for (int i = 0; i < STAR_SPAWN_COUNT; i++) {
        StarSprite newStar = new StarSprite(this.currentSpeed, this.getWidth(), this.getHeight());
        starSet.add(newStar);
      }
      nextStarAt = System.currentTimeMillis() + STAR_SPACING;
    }

    if (boxianCountSinceLastSpeedup >= SPEED_UP_THRESHOLD) {
      this.currentSpeed += 0.2;
      boxianCountSinceLastSpeedup = 0;
    }

    // Test collision among all boxians and against
    for (BoxianSprite boxianSprite : boxianSet) {
      if (boxianSet.findIntersecting(boxianSprite.bbox).size() > 1) {
        boxianSprite.isOverlapping = true;
      } else {
        boxianSprite.isOverlapping = false;
      }
    }

    // Test collisions between boxians and stars:
    for (BoxianSprite boxianSprite : boxianSet) {
      Set<StarSprite> intersectingStars = starSet.findIntersecting(boxianSprite.bbox);
      if (intersectingStars.size() > 0) {
        for (StarSprite starSprite : intersectingStars) {
          starSprite.colliding = true;
        }
      }
    }

    // Test collisions between stars and stars:
    for (StarSprite starSprite : starSet) {
      Set<StarSprite> intersectingStars = starSet.findIntersecting(starSprite.bbox);
      if (intersectingStars.size() > 1) {
        for (StarSprite otherStarSprite : intersectingStars) {
          otherStarSprite.colliding = true;
        }
      }
    }

    // Now test collision detection for the lasers:
    for (LaserSprite laserBeam : laserBeams) {
      for (Iterator<BoxianSprite> it = boxianSet.iterator(); it.hasNext();) {
        BoxianSprite boxian = it.next();
        if (boxian.intersects(laserBeam)) {
          boxian.laserHit();
          laserBeam.active = false;
        }
        if (!boxian.active) {
          it.remove();
        }
      }
    }

    if (state == GameState.GAME_ON && boxianSet.findIntersecting(player.bbox).size() > 0) {
      state = GamePanel.GameState.GAME_OVER;
    }
  }

  /**
   * Draw everything.
   * 
   * @param g The current graphics context.
   */
  public void paint(Graphics g) {
    super.paint(g);

    for (StarSprite star : starSet) {
      star.draw(g.create());
    }

    for (BoxianSprite boxian : boxianSet) {
      boxian.draw(g.create());
    }

    if (state == GameState.GAME_ON) {
      player.draw(g.create());
    }

    for (LaserSprite laserBeam : laserBeams) {
      laserBeam.draw(g.create());
    }

    // Paint the HUD
    paintHud(g);
  }

  /**
   * Draw the heads up display.
   * 
   * @param g The graphics context.
   */
  public void paintHud(Graphics g) {
    String ammoString;
    if (rechargeMillis < System.currentTimeMillis()) {
      ammoString = "AMMO " + numShots;
    } else {
      ammoString =
          "AMMO RECHARGING " + (int) (1 + ((rechargeMillis - System.currentTimeMillis()) / 1000.0));
    }
    g.setColor(Color.white);
    g.drawString(ammoString, 30, 30);
    g.drawString("FPS " + fps, 30, 45);

    if (state == GameState.GAME_OVER) {
      g.setColor(Color.orange);
      g.drawString("GAME OVER. Press 'r' to restart.", 400, 300);
    }
  }

  /**
   * Set up the key listeners to receive keyboard input for the game.
   */
  public void registerKeyListeners() {
    this.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {}

      @Override
      public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP:
            player.upThrustersOn();
            break;
          case KeyEvent.VK_DOWN:
            player.downThrustersOn();
            break;
          case KeyEvent.VK_SPACE:
            spawnLaser = true;
            break;
          default:
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP:
            if (state == GameState.GAME_ON) {
              player.upThrustersOff();
            }
            break;
          case KeyEvent.VK_DOWN:
            if (state == GameState.GAME_ON) {
              player.downThrustersOff();
            }
            break;
          case KeyEvent.VK_SPACE:
            if (state == GameState.GAME_ON) {
              spawnLaser = false;
            }
            break;
          case KeyEvent.VK_R:
            if (state == GameState.GAME_OVER) {
              GamePanel.this.initGame();
            }
            break;
          default:
        }
      }

    });
  }

  // Private member data
  private boolean gameRunning = true; // set to false to stop game
  private Thread gameThread = null; // the running game thread
  private long lastLoopTime; // last time the game loop was run
  private boolean spawnLaser = false; // whether the laser key is down
  protected static long BOXIAN_SPACING = 150; // How frequently to spawn boxians
  protected static long STAR_SPACING = 1; // How frequently to spawn stars
  // How many boxians to add before speeding up:
  protected static int SPEED_UP_THRESHOLD = 1000;
  protected static int RELOAD_AMMO_COUNT = 50; // How many lasers to reload
  // How many stars to create on each spawn:
  protected static int STAR_SPAWN_COUNT = 4;

  // What state the game is currently in
  protected GameState state;

  // Possible game states
  protected enum GameState {
    GAME_ON, GAME_OVER
  }

  // Static game helpers
  public static Random random = new Random();
}
