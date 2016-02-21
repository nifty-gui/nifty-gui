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

public class PathElementArc implements PathElement {
  private double x;
  private double y;
  private double r;
  private double startAngle;
  private double endAngle;
  private final boolean startNewLine;
  private Vec2 pathLastPoint;

  public PathElementArc(final double x, final double y, final double r, final double startAngle, final double endAngle, final boolean startNewLine) {
    this.x = x;
    this.y = y;
    this.r = r;
    this.startAngle = startAngle;
    this.endAngle = endAngle;
    this.startNewLine = startNewLine;
  }

  @Override
  public Vec2 getPathPoint(final Vec2 pathLastPoint) {
    if (this.startNewLine) {
      if (pathLastPoint == null) {
        throw new NiftyRuntimeException("lineTo with startNewLine (preceding element is moveTo) but no pathLastPoint");
      }
      this.pathLastPoint = new Vec2(pathLastPoint);
    }

    float endX = (float) (Math.cos(endAngle) * r + x);
    float endY = (float) (Math.sin(endAngle) * r + y);
    return new Vec2(endX, endY);
  }

  @Override
  public void stroke(final LineParameters lineParameters, final Mat4 transform, final BatchManager batchManager) {
    int i = 0;
    if (startNewLine) {
      batchManager.addFirstLineVertex(pathLastPoint.getX(), pathLastPoint.getY(), transform, lineParameters);
      i = 1;
    }

    double cx = 0.0;
    double cy = 0.0;
    for (; i<64; i++) {
      double t = i / (double) (64 - 1);

      double angle = startAngle + t * (endAngle - startAngle);
      cx = x + r * Math.cos(angle);
      cy = y + r * Math.sin(angle);
      batchManager.addLineVertex((float) cx, (float) cy, transform, lineParameters);
    }
  }

  @Override
  public void fill(final Mat4 transform, final BatchManager batchManager) {
    for (int i=0; i<64; i++) {
      double t = i / (double) (64 - 1);

      double angle = startAngle + t * (endAngle - startAngle);
      double cx = x + r * Math.cos(angle);
      double cy = y + r * Math.sin(angle);
      batchManager.addTriangleFanVertex((float) cx, (float) cy, transform);
    }
  }
}
