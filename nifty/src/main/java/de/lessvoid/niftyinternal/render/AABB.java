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
package de.lessvoid.niftyinternal.render;

import de.lessvoid.niftyinternal.common.Box;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.math.Vec4;
import de.lessvoid.nifty.spi.NiftyTexture;

/**
 * This helper class keeps track of two AABB representing the old and the current position of a RenderNode.
 * @author void
 */
class AABB {
  private final Box old = new Box();
  private final Box current = new Box();

  AABB(final int width, final int height) {
    current.setX(0);
    current.setY(0);
    current.setWidth(width);
    current.setHeight(height);
  }

  /**
   * Update the old aabb with the current state and then update the current state to a new position.
   * @param my
   * @param width
   * @param height
   */
  void update(final Mat4 my, final int width, final int height) {
    old.from(current);
    updateAABB(current, my, width, height);
  }

  void markStencil(final NiftyTexture renderTarget) {
//    renderTarget.markStencil(old.getX(), old.getY(), old.getWidth(), old.getHeight());
//    renderTarget.markStencil(current.getX(), current.getY(), current.getWidth(), current.getHeight());
  }

  void output(final StringBuilder result, final String offset) {
    outputAABB(attributesOffset(result, offset), old, "AABB old");
    outputAABB(attributesOffset(result, offset), current, "AABB cur");
  }

  private void updateAABB(final Box target, final Mat4 local, final int width, final int height) {
    Vec4 topLeft = new Vec4(0.f, 0.f, 0.f, 1.f);
    Vec4 topRight = new Vec4(width, 0.f, 0.f, 1.f);
    Vec4 bottomRight = new Vec4(width, height, 0.f, 1.f);
    Vec4 bottomLeft = new Vec4(0, height, 0.f, 1.f);
    Vec4 topLeftT = Mat4.transform(local, topLeft);
    Vec4 topRightT = Mat4.transform(local, topRight);
    Vec4 bottomRightT = Mat4.transform(local, bottomRight);
    Vec4 bottomLeftT = Mat4.transform(local, bottomLeft);
    float minX = Math.min(topLeftT.x, Math.min(topRightT.x, Math.min(bottomRightT.x, bottomLeftT.x)));
    float maxX = Math.max(topLeftT.x, Math.max(topRightT.x, Math.max(bottomRightT.x, bottomLeftT.x)));
    float minY = Math.min(topLeftT.y, Math.min(topRightT.y, Math.min(bottomRightT.y, bottomLeftT.y)));
    float maxY = Math.max(topLeftT.y, Math.max(topRightT.y, Math.max(bottomRightT.y, bottomLeftT.y)));
    target.setX((int) minX);
    target.setY((int) minY);
    target.setWidth((int) (maxX - minX));
    target.setHeight((int) (maxY - minY));
  }

  private StringBuilder attributesOffset(final StringBuilder result, final String offset) {
    return result.append(offset + "  ");
  }

  private void outputAABB(final StringBuilder result, final Box box, final String name) {
    result.append(name).append(" ");
    box.toString(result);
    result.append("\n");
  }
}
