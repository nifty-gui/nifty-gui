package de.lessvoid.nifty.tools;

import java.util.ArrayList;

/**
 * A value computed based on linear interpolation between a set of points.
 * 
 * @author void
 */
public class LinearInterpolator {
  private ArrayList<Point> curve = new ArrayList<Point>();
  
  public void addPoint(final float x, final float y) {
    curve.add(new Point(x, y));
  }

  public float prepare() {
    float maxX = calcMaxX(curve);
    for (Point p : curve) {
      p.x = p.x / maxX;
    }
    return maxX;
  }

  public float getValue(final float x) {
    Point p0 = curve.get(0);
    for (int i = 1; i < curve.size(); i++) {
      Point p1 = curve.get(i);
      if (isInInterval(x, p0, p1)) {
        return calcValue(x, p0, p1);
      }
      p0 = p1;
    }
    if (x > curve.get(curve.size() - 1).x) {
      return curve.get(curve.size() - 1).y;
    } else if (x < curve.get(0).x) {
      return curve.get(0).y;
    } else {
      return 0.0f;
    }
  }

  private boolean isInInterval(final float x, final Point p0, final Point p1) {
    return x >= p0.x && x <= p1.x;
  }

  private float calcValue(final float x, final Point p0, final Point p1) {
    float st = (x - p0.x) / (p1.x - p0.x);
    return p0.y + st * (p1.y - p0.y);
  }

  private float calcMaxX(final ArrayList<Point> curve) {
    float maxX = -1.0f;
    for (Point p : curve) {
      if (p.x > maxX) {
        maxX = p.x;
      }
    }
    return maxX;
  }

  public class Point {
    public float x;
    public float y;
    
    public Point(final float x, final float y) {
      this.x = x;
      this.y = y;
    }
  }
}
