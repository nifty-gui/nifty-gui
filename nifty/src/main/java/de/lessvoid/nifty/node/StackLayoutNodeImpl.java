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

package de.lessvoid.nifty.node;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.types.Point;
import de.lessvoid.nifty.types.Rect;
import de.lessvoid.nifty.types.Size;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * The layout node implementation for a stack layout.
 *
 * <p>This layout type is able to place multiple child nodes in either horizontal or vertical orientation.</p>
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class StackLayoutNodeImpl extends AbstractLayoutNodeImpl {
  @Nonnull
  private Orientation orientation;
  private boolean stretchLast;

  StackLayoutNodeImpl(@Nonnull final Orientation orientation, final boolean stretchLast) {
    this.orientation = orientation;
    this.stretchLast = stretchLast;
  }

  @Nonnull
  @Override
  protected Size measureInternal(@Nonnull final Size availableSize) {
    Collection<NiftyNode> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements, means that we do not require any size. */
      return Size.ZERO;
    }

    Size remainingSize = availableSize;
    Size requiredSize = Size.ZERO;
    for (NiftyNode child : children) {
      Size childSize = getLayout().measure(child, remainingSize);
      if (orientation == Orientation.Horizontal) {
        requiredSize = new Size(requiredSize.getWidth() + childSize.getWidth(),
            Math.max(requiredSize.getHeight(), childSize.getHeight()));
        if (!Float.isInfinite(remainingSize.getWidth())) {
          remainingSize = new Size(remainingSize.getWidth() - childSize.getWidth(), remainingSize.getHeight());
        }
      } else {
        requiredSize = new Size(Math.max(requiredSize.getWidth(), childSize.getWidth()),
            requiredSize.getHeight() + childSize.getHeight());
        if (!Float.isInfinite(remainingSize.getHeight())) {
          remainingSize = new Size(remainingSize.getWidth(), remainingSize.getHeight() - childSize.getHeight());
        }
      }
    }

    return requiredSize;
  }

  @Override
  protected void arrangeInternal(@Nonnull final Rect area) {
    List<NiftyNode> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements -> We are all done. */
      return;
    }

    Point currentOrigin = area.getOrigin();
    Size remainingSize = area.getSize();

    int childrenCount = children.size();
    for (int i = 0; i < childrenCount; i++) {
      NiftyNode child = children.get(i);
      Size childSize = getLayout().getDesiredSize(child);
      Size arrangedSize;
      Point nextOrigin;
      if ((i == (childrenCount - 1)) && stretchLast) {
        arrangedSize = remainingSize;
        nextOrigin = currentOrigin; // never used
      } else if (orientation == Orientation.Horizontal) {
        arrangedSize = new Size(Math.min(remainingSize.getWidth(), childSize.getWidth()), remainingSize.getHeight());
        remainingSize = new Size(remainingSize.getWidth() - arrangedSize.getWidth(), remainingSize.getHeight());
        nextOrigin = new Point(currentOrigin.getX() + arrangedSize.getWidth(), currentOrigin.getY());
      } else {
        arrangedSize = new Size(remainingSize.getWidth(), Math.min(remainingSize.getHeight(), childSize.getHeight()));
        remainingSize = new Size(remainingSize.getWidth(), remainingSize.getHeight() - arrangedSize.getHeight());
        nextOrigin = new Point(currentOrigin.getX(), currentOrigin.getY() + arrangedSize.getHeight());
      }

      getLayout().arrange(child, new Rect(currentOrigin, arrangedSize));
      currentOrigin = nextOrigin;
    }
  }

  @Nonnull
  Orientation getOrientation() {
    return orientation;
  }

  void setOrientation(@Nonnull final Orientation orientation) {
    if (this.orientation != orientation) {
      this.orientation = orientation;
      invalidateMeasure();
      invalidateArrange();
    }
  }

  boolean isStretchLast() {
    return stretchLast;
  }

  void setStretchLast(final boolean stretchLast) {
    if (this.stretchLast != stretchLast) {
      this.stretchLast = stretchLast;
      invalidateArrange();
    }
  }

  @Override
  public void initialize(final Nifty nifty, final NiftyNode niftyNode) {

  }

  @Override
  protected NiftyNode createNode() {
    return new StackLayoutNode(this);
  }
}
