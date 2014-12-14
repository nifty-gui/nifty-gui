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

import de.lessvoid.nifty.internal.canvas.Context;
import de.lessvoid.nifty.internal.canvas.LineParameters;
import de.lessvoid.nifty.internal.canvas.path.CubicBezier.Renderer;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.math.Vec2;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class PathElementCubicBezier implements PathElement {
  private final double cp1x;
  private final double cp1y;
  private final double cp2x;
  private final double cp2y;
  private final double x;
  private final double y;
  private final boolean hasPrecedingMoveTo;

  public PathElementCubicBezier(
      final double cp1x,
      final double cp1y,
      final double cp2x,
      final double cp2y,
      final double x,
      final double y,
      final boolean hasPrecedingMoveTo) {
    this.cp1x = cp1x;
    this.cp1y = cp1y;
    this.cp2x = cp2x;
    this.cp2y = cp2y;
    this.x = x;
    this.y = y;
    this.hasPrecedingMoveTo = hasPrecedingMoveTo;
  }

  @Override
  public Vec2 stroke(
      final LineParameters lineParameters,
      final Mat4 transform,
      final BatchManager batchManager,
      final Vec2 currentPos) {
    if (hasPrecedingMoveTo) {
      batchManager.addFirstLineVertex(currentPos.getX(), currentPos.getY(), transform, lineParameters);
    }

    CubicBezier curve = new CubicBezier(
        currentPos,
        new Vec2((float) cp1x, (float) cp1y),
        new Vec2((float) cp2x, (float) cp2y),
        new Vec2((float) x, (float) y));
    curve.renderCurve(new Renderer() {
      @Override
      public void addLineVertex(float x, float y) {
        batchManager.addLineVertex((float) x, (float) y, transform, lineParameters);    
      }
    });
    return new Vec2((float) x, (float) y);
  } 

  @Override
  public void fill(Context context, BatchManager batchManager, boolean first, boolean last) {
  }
}
