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

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.NiftyRuntimeException;
import de.lessvoid.niftyinternal.canvas.LineParameters;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.math.Vec2;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

/**
 * This class receives all path related calls from the Context class and takes care of rendering
 * stroked or filled paths.
 *
 * @author void
 */
public class PathRenderer {
  // we keep all added PathElements in this list
  private List<PathElement> path;

  // we keep the very first vertex added to the path around (in case someone calls closePath)
  private Vec2 pathStartVertex;

  // additionally when we actually produce output we remember here (for closePath later)
  private boolean willProduceOutput;

  public void beginPath() {
    path = new ArrayList<PathElement>();
    pathStartVertex = null;
    willProduceOutput = false;
  }

  public void moveTo(final double x, final double y) {
    assertPath();
    path.add(new PathElementMoveTo(x, y));
    pathStartVertex = new Vec2((float) x, (float) y);
    willProduceOutput = false;
  }

  public void lineTo(final double x, final double y) {
    assertPath();

    // When the preceding path element is a moveTo command then we need to start a new line.
    // So here we provide the information to PathElementLineTo so that it can start a new batch.
    path.add(new PathElementLineTo(x, y, isLastPathElementMoveTo()));

    // if we don't have a pathStartVertex yet (nobody called moveTo) then we start the current
    // vertex as the new pathStartVertex so that we can close the path later (if requested).
    if (pathStartVertex == null) {
      pathStartVertex = new Vec2((float) x, (float) y);
    }

    willProduceOutput = true;
  }

  public void arc(final double x, final double y, final double r, final double startAngle, final double endAngle) {
    assertPath();

    float startX = (float) (Math.cos(startAngle) * r + x);
    float startY = (float) (Math.sin(startAngle) * r + y);
    if (pathStartVertex == null) {
      pathStartVertex = new Vec2(startX, startY);
    }

    if (!path.isEmpty()) {
      lineTo(startX, startY);
    }
    path.add(new PathElementArc(x, y, r, startAngle, endAngle, isLastPathElementMoveTo()));

    willProduceOutput = true;

    float endX = (float) (Math.cos(endAngle) * r + x);
    float endY = (float) (Math.sin(endAngle) * r + y);
    path.add(new PathElementMoveTo(endX, endY));
  }

  public void bezierCurveTo(double cp1x, double cp1y, double cp2x, double cp2y, double x, double y) {
    assertPath();

    path.add(new PathElementCubicBezier(cp1x, cp1y, cp2x, cp2y, x, y, isLastPathElementMoveTo()));
    path.add(new PathElementMoveTo(x, y));

    willProduceOutput = true;
  }

  public void closePath() {
    if (pathStartVertex != null && willProduceOutput) {
      lineTo(pathStartVertex.getX(), pathStartVertex.getY());
    }
  }

  public void strokePath(
      final LineParameters lineParameters,
      final Mat4 transform,
      final BatchManager batchManager) {
    assertPath();

    Vec2 currentPathPos = null;
    for (int i=0; i<path.size(); i++) {
      currentPathPos = path.get(i).stroke(lineParameters, transform, batchManager, currentPathPos);
    }
  }

  public void fillPath(
      final Mat4 transform,
      final BatchManager batchManager,
      final LineParameters fillOutlineParameters) {
    assertPath();

    // TODO is this actual a correct way to do it?
    // we need to close an eventual open path but only for the fill
    // so we kinda try to save the current path here to restore it later
    Vec2 save = new Vec2(pathStartVertex);
    List<PathElement> backup = new ArrayList<>(path);

    closePath();

    for (int i=0; i<path.size(); i++) {
      path.get(i).fill(transform, batchManager);
    }

    Vec2 currentPathPos = null;
    for (int i=0; i<path.size(); i++) {
      currentPathPos = path.get(i).stroke(fillOutlineParameters, transform, batchManager, currentPathPos);
    }

    // restore the path
    pathStartVertex = new Vec2(save);
    path = new ArrayList<>(backup);
  }

  private boolean isLastPathElementMoveTo() {
    if (path.isEmpty()) {
      return false;
    }
    PathElement element = path.get(path.size() - 1);
    return element instanceof PathElementMoveTo;
  }

  private void assertPath() {
    if (path == null) {
      throw new NiftyRuntimeException("no active path - did you forget to call beginPath()?");
    }
  }
}
