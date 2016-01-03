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

import de.lessvoid.nifty.NiftyLayout;
import de.lessvoid.nifty.spi.node.NiftyLayoutNodeImpl;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;
import de.lessvoid.nifty.types.NiftyPoint;
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class AlignmentLayoutNodeImpl extends AbstractLayoutNodeImpl<AlignmentLayoutNode> {
  @Nonnull
  @Override
  protected NiftySize measureInternal(@Nonnull final NiftySize availableSize) {
    /* Since this layout does not prevent overlapping the max size of a single element counts. */
    NiftySize childSize = NiftySize.ZERO;
    NiftyLayout layout = getLayout();
    for (NiftyNodeImpl<?> child : layout.getDirectChildren(this)) {
      childSize = NiftySize.max(layout.measure(child, availableSize), childSize);
    }
    return childSize;
  }

  @Override
  protected void arrangeInternal(@Nonnull final NiftyRect area) {
    NiftyLayout layout = getLayout();
    for (NiftyLayoutNodeImpl<?> child : layout.getChildLayoutNodes(this)) {
      Horizontal horizontal = Horizontal.Stretch;
      Vertical vertical = Vertical.Stretch;
      if (child instanceof AlignmentLayoutChildNodeImpl) {
        AlignmentLayoutChildNodeImpl childNode = (AlignmentLayoutChildNodeImpl) child;
        horizontal = childNode.getHorizontal();
        vertical = childNode.getVertical();
      }

      if (horizontal == Horizontal.Stretch && vertical == Vertical.Stretch) {
        layout.arrange(child, area);
      } else {
        NiftySize desiredSize = child.getDesiredSize();
        float childWidth = horizontal == Horizontal.Stretch ? area.getSize().getWidth() : desiredSize.getWidth();
        float childHeight = vertical == Vertical.Stretch ? area.getSize().getHeight() : desiredSize.getHeight();

        if (area.getSize().equals(childWidth, childHeight)) {
          layout.arrange(child, area);
        } else {
          float offsetX = 0.f;
          float offsetY = 0.f;
          switch (horizontal) {
            case Center: offsetX = (area.getSize().getWidth() / 2.f) - (desiredSize.getWidth() / 2.f); break;
            case Right: offsetX = area.getSize().getWidth() - desiredSize.getWidth(); break;
          }
          switch (vertical) {
            case Middle: offsetY = (area.getSize().getHeight() / 2.f) - (desiredSize.getHeight() / 2.f); break;
            case Bottom: offsetY = area.getSize().getHeight() - desiredSize.getHeight(); break;
          }

          NiftyPoint newOrigin = area.getOrigin().add(offsetX, offsetY);
          NiftySize newSize = desiredSize.equals(childWidth, childHeight) ?
              desiredSize : NiftySize.newNiftySize(childWidth, childHeight);

          layout.arrange(child, NiftyRect.newNiftyRect(newOrigin, newSize));
        }
      }
    }
  }

  @Override
  protected AlignmentLayoutNode createNode() {
    return new AlignmentLayoutNode(this);
  }
}
