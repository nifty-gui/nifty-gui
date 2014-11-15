package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.math.Vec2;

/**
 * A CubicBezier curve that will be subdivided into lines.
 * @author void
 */
public class CubicBezier {
  Vec2 b0;
  Vec2 b1;
  Vec2 b2;
  Vec2 b3;

  CubicBezier() {
    this.b0 = new Vec2();
    this.b1 = new Vec2();
    this.b2 = new Vec2();
    this.b3 = new Vec2();
  }

  CubicBezier(final Vec2 b0, final Vec2 b1, final Vec2 b2, final Vec2 b3) {
    this.b0 = b0;
    this.b1 = b1;
    this.b2 = b2;
    this.b3 = b3;
  }

  public void output(final Context context) {
    context.lineTo(b3.getX(), b3.getY());
  }

  public void subdivide(final CubicBezier left, final CubicBezier right) {
    float z = 0.5f;
    float z_1 = z - 1.f;

    // left
    // b0 =       b0
    left.b0 = new Vec2(b0);

    // b1 =     z*b1 -       (z-1)*b0
    Vec2.sub(new Vec2(b1).scale(z), new Vec2(b0).scale(z_1), left.b1);

    // b2 =   z*z*b2 -   2*z*(z-1)*b1 +     (z-1)*(z-1)*b0
    Vec2.sub(new Vec2(b2).scale(z*z), new Vec2(b1).scale(2*z*z_1), left.b2);
    Vec2.add(left.b2, new Vec2(b0).scale(z_1*z_1), left.b2);

    // b3 = z*z*z*b3 - 3*z*z*(z-1)*b2 + 3*z*(z-1)*(z-1)*b1 - (z-1)*(z-1)*(z-1)*b0
    Vec2.sub(new Vec2(b3).scale(z*z*z), new Vec2(b2).scale(3*z*z*z_1), left.b3);
    Vec2.add(left.b3, new Vec2(b1).scale(3*z*z_1*z_1), left.b3);
    Vec2.sub(left.b3, new Vec2(b0).scale(z_1*z_1*z_1), left.b3);

    // right
    // b0 = z*z*z*b3 - 3*z*z*(z-1)*b2 + 3*z*(z-1)*(z-1)*b1 - (z-1)*(z-1)*(z-1)*b0
    Vec2.sub(new Vec2(b3).scale(z*z*z), new Vec2(b2).scale(3*z*z*z_1), right.b0);
    Vec2.add(right.b0, new Vec2(b1).scale(3*z*z_1*z_1), right.b0);
    Vec2.sub(right.b0, new Vec2(b0).scale(z_1*z_1*z_1), right.b0);

    // b1 =   z*z*b3 -   2*z*(z-1)*b2 +     (z-1)*(z-1)*b1
    Vec2.sub(new Vec2(b3).scale(z*z), new Vec2(b2).scale(2*z*z_1), right.b1);
    Vec2.add(right.b1, new Vec2(b1).scale(z_1*z_1), right.b1);

    // b2 =     z*b3 -       (z-1)*b2
    Vec2.sub(new Vec2(b3).scale(z), new Vec2(b2).scale(z_1), right.b2);

    // b3 =       b3
    right.b3 = new Vec2(b3);
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("b0 = " + b0).append("\n");
    result.append("b1 = " + b1).append("\n");
    result.append("b2 = " + b2).append("\n");
    result.append("b3 = " + b3).append("\n");
    return result.toString();
  }
}