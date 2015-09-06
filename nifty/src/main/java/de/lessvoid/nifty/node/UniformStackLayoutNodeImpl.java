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

import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.NiftyPoint;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;
import java.util.Collection;

import static de.lessvoid.nifty.types.NiftyPoint.newNiftyPoint;

/**
 * The layout node implementation for a uniform stack layout.
 *
 * <p>This layout type is able to place multiple child nodes in either horizontal or vertical orientation. Every
 * child element is the same size assigned</p>
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class UniformStackLayoutNodeImpl extends AbstractLayoutNodeImpl<UniformStackLayoutNode> {
  @Nonnull
  private Orientation orientation;

  UniformStackLayoutNodeImpl(@Nonnull final Orientation orientation) {
    this.orientation = orientation;
  }

  @Override
  @Nonnull
  protected NiftySize measureInternal(@Nonnull final NiftySize availableSize) {
    Collection<NiftyNodeImpl<?>> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements, means that we do not require any size. */
      return NiftySize.ZERO;
    }

    NiftySize sizePerChild = getSizePerChild(availableSize, children.size());

    NiftySize largestSizeRequested = NiftySize.ZERO;
    for (NiftyNodeImpl<?> node : children) {
      NiftySize nodeSize = getLayout().measure(node, sizePerChild);
      largestSizeRequested = NiftySize.max(nodeSize, largestSizeRequested);
    }

    if (orientation == Orientation.Horizontal) {
      return NiftySize.newNiftySize(largestSizeRequested.getWidth() * children.size(), largestSizeRequested.getHeight());
    } else {
      return NiftySize.newNiftySize(largestSizeRequested.getWidth(), largestSizeRequested.getHeight() * children.size());
    }
  }

  @Override
  protected void arrangeInternal(@Nonnull final NiftyRect area) {
    Collection<NiftyNodeImpl<?>> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements -> We are all done. */
      return;
    }

    NiftySize sizePerChild = getSizePerChild(area.getSize(), children.size());
    NiftyPoint currentOrigin = area.getOrigin();
    for (NiftyNodeImpl<?> child : children) {
      getLayout().arrange(child, NiftyRect.newNiftyRect(currentOrigin, sizePerChild));

      /* Move the origin along the orientation */
      if (orientation == Orientation.Horizontal) {
        currentOrigin = newNiftyPoint(currentOrigin.getX() + sizePerChild.getWidth(), currentOrigin.getY());
      } else {
        currentOrigin = newNiftyPoint(currentOrigin.getX(), currentOrigin.getY() + sizePerChild.getHeight());
      }
    }
  }

  @Override
  protected UniformStackLayoutNode createNode() {
    return new UniformStackLayoutNode(this);
  }

  @Nonnull
  private NiftySize getSizePerChild(@Nonnull final NiftySize outerSize, final int childCount) {
    NiftySize sizePerChild;
    if (orientation == Orientation.Horizontal) {
      /* Uniform Horizontal Stacking. Each element is assigned a equal share of the total width */
      if (Float.isInfinite(outerSize.getWidth())) {
        sizePerChild = outerSize;
      } else {
        sizePerChild = NiftySize.newNiftySize(outerSize.getWidth() / childCount, outerSize.getHeight());
      }
    } else {
      /* Uniform Vertical Stacking. Each element is assigned a equal share of the total height */
      if (Float.isInfinite(outerSize.getHeight())) {
        sizePerChild = outerSize;
      } else {
        sizePerChild = NiftySize.newNiftySize(outerSize.getWidth(), outerSize.getHeight() / childCount);
      }
    }
    return sizePerChild;
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
}
