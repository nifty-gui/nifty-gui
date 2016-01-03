/*
 * Copyright (c) 2016, Nifty GUI Community
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
package de.lessvoid.nifty.node;

import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.NiftyPoint;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;
import java.util.Collection;

import static de.lessvoid.nifty.types.NiftyPoint.newNiftyPointWithOffset;

/**
 * This is the implementation of a layout node that support padding. It will apply some padding to any child layout
 * node.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class PaddingLayoutNodeImpl extends AbstractLayoutNodeImpl<PaddingLayoutNode> {
  /**
   * The padding applied to the bottom.
   */
  private float bottom;
  /**
   * The padding applied to the left side.
   */
  private float left;
  /**
   * The padding applied to the right side.
   */
  private float right;
  /**
   * The padding applied to the top.
   */
  private float top;

  PaddingLayoutNodeImpl(final float top, final float right, final float bottom, final float left) {
    if (!(Math.abs(top) <= Float.MAX_VALUE)) throw new IllegalArgumentException("top is expected to be a finite value.");
    if (!(Math.abs(right) <= Float.MAX_VALUE)) throw new IllegalArgumentException("right is expected to be a finite value.");
    if (!(Math.abs(bottom) <= Float.MAX_VALUE)) throw new IllegalArgumentException("bottom is expected to be a finite value.");
    if (!(Math.abs(left) <= Float.MAX_VALUE)) throw new IllegalArgumentException("left is expected to be a finite value.");

    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
  }

  float getBottom() {
    return bottom;
  }

  void setBottom(final float bottom) {
    if (!(Math.abs(bottom) <= Float.MAX_VALUE)) throw new IllegalArgumentException("bottom is expected to be a finite value.");

    if (this.bottom != bottom){
      this.bottom = bottom;

      invalidateMeasure();
    }
  }

  float getLeft() {
    return left;
  }

  void setLeft(final float left) {
    if (!(Math.abs(left) <= Float.MAX_VALUE)) throw new IllegalArgumentException("left is expected to be a finite value.");

    if (this.left != left) {
      this.left = left;

      invalidateMeasure();
    }
  }

  float getRight() {
    return right;
  }

  void setRight(final float right) {
    if (!(Math.abs(right) <= Float.MAX_VALUE)) throw new IllegalArgumentException("right is expected to be a finite value.");

    if (this.right != right) {
      this.right = right;

      invalidateMeasure();
    }
  }

  float getTop() {
    return top;
  }

  void setTop(final float top) {
    if (!(Math.abs(top) <= Float.MAX_VALUE)) throw new IllegalArgumentException("top is expected to be a finite value.");

    if (this.top != top) {
      this.top = top;

      invalidateMeasure();
    }
  }

  @Nonnull
  @Override
  protected NiftySize measureInternal(@Nonnull final NiftySize availableSize) {
    Collection<NiftyNodeImpl<?>> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements, means that we do not require any size. */
      return NiftySize.newNiftySize(left + right, top + bottom);
    }

    NiftySize childSize = NiftySize.ZERO;
    for (NiftyNodeImpl<?> child : children) {
      childSize = NiftySize.max(getLayout().measure(child, availableSize), childSize);
    }

    return NiftySize.newNiftySize(left + right + childSize.getWidth(), top + bottom + childSize.getHeight());
  }

  @Override
  protected void arrangeInternal(@Nonnull final NiftyRect area) {
    Collection<NiftyNodeImpl<?>> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements -> We are all done. */
      return;
    }

    NiftyPoint newOrigin = newNiftyPointWithOffset(area.getOrigin(), left, top);
    NiftySize newSize = NiftySize.newNiftySize(area.getSize().getWidth() - left - right, area.getSize().getHeight() - top - bottom);
    NiftyRect newArea = NiftyRect.newNiftyRect(newOrigin, newSize);

    for (NiftyNodeImpl<?> child : children) {
      getLayout().arrange(child, newArea);
    }
  }

  @Override
  protected PaddingLayoutNode createNode() {
    return new PaddingLayoutNode(this);
  }
}
