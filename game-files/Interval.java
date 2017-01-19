
/**
 * A class for storing a 1D interval and testing intersections between intervals.
 * 
 * @author John C. Bowers bowersjc@jmu.edu
 */
public class Interval {

  protected double min;
  protected double max;

  /**
   * @param min Minimum interval value. 
   * @param max Maximum interval value.
   */
  public Interval(double min, double max) {
    this.min = min;
    this.max = max;
  }

  /**
   * @param interval The interval to test intersection against.
   * @return True if the this intersects interval, false otherwise. 
   */
  public boolean intersects(Interval interval) {
    return !(min > interval.max || max < interval.min);
  }

  /**
   * 
   * @return The length of this interval.
   */
  public double length() {
    return max - min;
  }

  /**
   * 
   * @return The midpoint of this interval.
   */
  public double mid() {
    return (max + min) * 0.5;
  }
}
