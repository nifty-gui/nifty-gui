/*
 * Copyright (c) 2014, Jens Hohmuth 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.internal.canvas.path;

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

  interface Renderer {
    void addLineVertex(float x, float y);
  }

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

  public void renderCurve(final Renderer renderer) {
    renderCurve(this, renderer);
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("b0 = " + b0).append("\n");
    result.append("b1 = " + b1).append("\n");
    result.append("b2 = " + b2).append("\n");
    result.append("b3 = " + b3).append("\n");
    return result.toString();
  }

  private void output(final Renderer renderer) {
    renderer.addLineVertex(b3.getX(), b3.getY());
  }

  private void renderCurve(final CubicBezier c, final Renderer renderer) {
    if (isSufficientlyFlat(c)) {
      c.output(renderer);
      return;
    }

    CubicBezier left = new CubicBezier();
    CubicBezier right = new CubicBezier();
    c.subdivide(left, right);

    renderCurve(left, renderer);
    renderCurve(right, renderer);
  }

  private boolean isSufficientlyFlat(final CubicBezier c) {
    double ux = 3.0*c.b1.x - 2.0*c.b0.x - c.b3.x; ux *= ux;
    double uy = 3.0*c.b1.y - 2.0*c.b0.y - c.b3.y; uy *= uy;
    double vx = 3.0*c.b2.x - 2.0*c.b3.x - c.b0.x; vx *= vx;
    double vy = 3.0*c.b2.y - 2.0*c.b3.y - c.b0.y; vy *= vy;
    if (ux < vx) ux = vx;
    if (uy < vy) uy = vy;
    double tol = .25;
    double tolerance = 16*tol*tol;
    return (ux+uy <= tolerance);
  }
}
