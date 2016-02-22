/*
 * Copyright (c) 2015, Nifty GUI Community 
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
package de.lessvoid.niftyinternal.canvas.path;

import de.lessvoid.nifty.NiftyRuntimeException;
import de.lessvoid.niftyinternal.canvas.LineParameters;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.math.Vec2;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

import java.util.ArrayList;
import java.util.List;

/**
 * This class receives all elements related calls from the Context class and takes care of rendering
 * stroked or filled paths.
 *
 * @author void
 */
public class PathRenderer {
  private List<SubPath> path = new ArrayList<>();

  private static class SubPath {
    private List<PathElement> elements = new ArrayList<>();
    private boolean isClosed;
    private Vec2 lastPoint;

    SubPath() {
    }

    SubPath(final SubPath src) {
      this.elements = new ArrayList<>(src.elements);
      this.isClosed = src.isClosed;
      this.lastPoint = new Vec2(src.lastPoint);
    }

    void add(final PathElement pathElement) {
      elements.add(pathElement);
      lastPoint = pathElement.getPathPoint(lastPoint);
    }

    void markAsClosed() {
      isClosed = true;
    }

    PathElement getFirstPathElement() {
      return elements.get(0);
    }

    Vec2 getLastPoint() {
      return lastPoint;
    }

    void stroke(final LineParameters lineParameters, final Mat4 transform, final BatchManager batchManager) {
      if (elements.isEmpty()) {
        return;
      }
      if (elements.size() < 2) {
        return;
      }
      for (int i = 0; i< elements.size(); i++) {
        elements.get(i).stroke(lineParameters, transform, batchManager);
      }
      if (isClosed) {
        Vec2 startPoint = elements.get(0).getPathPoint(null);
        new PathElementLineTo(startPoint.getX(), startPoint.getY(), false).stroke(lineParameters, transform, batchManager);
      }
    }

    void fill(final Mat4 transform, final BatchManager batchManager) {
      for (int i = 0; i< elements.size(); i++) {
        elements.get(i).fill(transform, batchManager);
      }
    }

    boolean isLastElementMoveTo() {
      if (elements.isEmpty()) {
        return false;
      }
      return elements.get(elements.size() - 1) instanceof PathElementMoveTo;
    }
  }

  public void beginPath() {
    path.clear();
  }

  public void moveTo(final double x, final double y) {
    SubPath subPath = newSubPath();
    subPath.add(new PathElementMoveTo((float) x, (float) y));
    path.add(subPath);
  }

  public void closePath() {
    if (path.isEmpty()) {
      return;
    }

    SubPath lastSubPath = getLastSubPath();
    lastSubPath.markAsClosed();

    SubPath subPath = newSubPath();
    subPath.add(lastSubPath.getFirstPathElement());
    path.add(subPath);
  }

  public void lineTo(final double x, final double y) {
    if (ensureSubPath((float) x, (float) y)) {
      return;
    }

    SubPath lastSubPath = getLastSubPath();
    lastSubPath.add(new PathElementLineTo((float) x, (float) y, lastSubPath.isLastElementMoveTo()));
  }

  public void bezierCurveTo(final double cp1x, final double cp1y, final double cp2x, final double cp2y, final double x, final double y) {
    ensureSubPath((float) cp1x, (float) cp1y);

    SubPath lastSubPath = getLastSubPath();
    lastSubPath.add(new PathElementCubicBezier((float) cp1x, (float) cp1y, (float) cp2x, (float) cp2y, (float) x, (float) y, lastSubPath.isLastElementMoveTo()));
  }

  public void arcTo(final double x1, final double y1, final double x2, final double y2, final double r) {
    if (r < 0) {
      throw new NiftyRuntimeException("arcTo radius must not be negative");
    }

    ensureSubPath((float) x1, (float) y1);

    SubPath lastSubPath = getLastSubPath();
    Vec2 lastPoint = lastSubPath.getLastPoint();
    float x0 = lastPoint.getX();
    float y0 = lastPoint.getY();

    // If the point (x0, y0) is equal to the point (x1, y1),
    // or if the point (x1, y1) is equal to the point (x2, y2),
    // or if the radius radius is zero,
    if ((x0 == x1 && y0 == y1) ||
        (x1 == x2 && y1 == y2) ||
        (r == 0)) {
        // then the method must add the point (x1, y1) to the subpath,
        // and connect that point to the previous point (x0, y0) by a straight line.
        lineTo(x1, y1);
        return;
      }

    // TODO
    // Otherwise, if the points (x0, y0), (x1, y1), and (x2, y2) all lie on a single straight line,
    // then the method must add the point (x1, y1) to the subpath,
    // and connect that point to the previous point (x0, y0) by a straight line.

    // calc arc
    // 1. get all points
    Vec2 p0 = new Vec2(x0, y0);
    Vec2 p1 = new Vec2((float) x1, (float) y1);
    Vec2 p2 = new Vec2((float) x2, (float) y2);

    // 2. calc tangent vectors from p1 to p0 and to p2
    Vec2 t0 = Vec2.sub(p0, p1, null);
    Vec2 t1 = Vec2.sub(p2, p1, null);
    t0.normalise(t0);
    t1.normalise(t1);

    // 3. calc angle between tangents
    double angle = Math.acos(Vec2.dot(t0, t1));

    // 4. calc middle vector
    Vec2 tm = Vec2.add(t0, t1, null);
    tm.normalise(tm);

    // 5. calc circle center point
    double d = r / Math.sin(angle / 2);
    Vec2 circleCenter = Vec2.add(p1, new Vec2(tm).scale((float) d), null);

    // 6. calc vector from circle center point back to p1
    Vec2 back = Vec2.sub(p1, circleCenter, null);
    back.normalise(back);

    double backAngle = Math.atan2(back.getY(), back.getX());
    if (backAngle < 0) {
      backAngle += Math.PI * 2;
    }

    // 7. get new vector pointing to the tangents by rotating the back vector
    double arcAngle = Math.PI - Math.PI / 2 - angle / 2;
    double arcStart = backAngle - arcAngle;
    double arcEnd = backAngle + arcAngle;
    arc(circleCenter.getX(), circleCenter.getY(), r, arcStart, arcEnd);
  }

  public void arc(final double x, final double y, final double r, final double startAngle, final double endAngle) {
    float startX = (float) (Math.cos(startAngle) * r + x);
    float startY = (float) (Math.sin(startAngle) * r + y);

    if (!path.isEmpty()) {
      lineTo(startX, startY);
    }

    ensureSubPath(startX, startY);

    SubPath lastSubPath = getLastSubPath();
    lastSubPath.add(new PathElementArc(x, y, r, startAngle, endAngle, lastSubPath.isLastElementMoveTo()));
  }

  public void rect(final double x, final double y, final double width, final double height) {
    moveTo(x, y);
    lineTo(x + width, y);
    lineTo(x + width, y + height);
    lineTo(x, y + height);
    closePath();
  }

  public void strokePath(final LineParameters lineParameters, final Mat4 transform, final BatchManager batchManager) {
    for (int i=0; i<path.size(); i++) {
      path.get(i).stroke(lineParameters, transform, batchManager);
    }
  }

  public void fillPath(final Mat4 transform, final BatchManager batchManager, final LineParameters fillOutlineParameters) {
    List<SubPath> copy = pathCopy();
    for (int i=0; i<copy.size(); i++) {
      copy.get(i).markAsClosed();
      copy.get(i).fill(transform, batchManager);
    }
    for (int i=0; i<copy.size(); i++) {
      copy.get(i).stroke(fillOutlineParameters, transform, batchManager);
    }
  }

  private SubPath newSubPath() {
    return new SubPath();
  }

  private boolean ensureSubPath(final float x, final float y) {
    if (path.isEmpty()) {
      moveTo(x, y);
      return true;
    }
    return false;
  }

  private SubPath getLastSubPath() {
    return path.get(path.size() - 1);
  }

  private List<SubPath> pathCopy() {
    List<SubPath> result = new ArrayList<>();
    for (int i=0; i<path.size(); i++) {
      result.add(new SubPath(path.get(i)));
    }
    return result;
  }
}
