/**
 * A class for storing a box. The box is all points (x, y) where
 * minx <= x <= maxx and miny <= y <= maxy. 
 * 
 * @author John C. Bowers bowersjc@jmu.edu
 */
public class Box {

  protected Interval xint; // The x-bounds for this box
  protected Interval yint; // The y-bounds for this box
  
  /**
   * 
   * @param minx The minimum x bounds for this box.
   * @param maxx The maximum x bounds for this box.
   * @param miny The minimum y bounds for this box.
   * @param maxy The maximum y bounds for this box.
   */
  public Box(double minx, double maxx, double miny, double maxy) {
    xint = new Interval(minx, maxx);
    yint = new Interval(miny, maxy);
  }
  
  /**
   * 
   * @param box The box with which to interset. 
   * @return true if box intersects this.
   */
  public boolean intersects(Box box) {
    return xint.intersects(box.xint) && yint.intersects(box.yint);
  }
  
  /**
   * 
   * @return The minimum x bounds.
   */
  public double minx() {
    return xint.min;
  }
  
  /**
   * 
   * @return The maximum x bounds.
   */
  public double maxx() {
    return xint.max;
  }
  
  /**
   * 
   * @return The minimum y bounds.
   */
  public double miny() {
    return yint.min;
  }
  
  /**
   * 
   * @return The maximum y bounds.
   */
  public double maxy() {
    return yint.max;
  }
  
  /**
   * 
   * @return The width of the box.
   */
  public double width() {
    return xint.length();
  }
  
  /**
   * 
   * @return The height of the box.
   */
  public double height() {
    return yint.length();
  }
}
