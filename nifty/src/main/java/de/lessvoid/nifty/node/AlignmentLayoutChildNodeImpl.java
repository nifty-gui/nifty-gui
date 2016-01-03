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
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class AlignmentLayoutChildNodeImpl extends AbstractLayoutNodeImpl<AlignmentLayoutChildNode> {
  @Nonnull
  private Horizontal horizontal;
  @Nonnull
  private Vertical vertical;

  AlignmentLayoutChildNodeImpl(@Nonnull final Horizontal horizontal, @Nonnull final Vertical vertical) {
    this.horizontal = horizontal;
    this.vertical = vertical;
  }

  @Nonnull
  public Horizontal getHorizontal() {
    return horizontal;
  }

  public void setHorizontal(@Nonnull final Horizontal horizontal) {
    if (this.horizontal != horizontal) {
      this.horizontal = horizontal;
      invalidateArrange();
    }
  }

  @Nonnull
  public Vertical getVertical() {
    return vertical;
  }

  public void setVertical(@Nonnull final Vertical vertical) {
    if (this.vertical != vertical) {
      this.vertical = vertical;
      invalidateArrange();
    }
  }

  @Nonnull
  @Override
  protected NiftySize measureInternal(@Nonnull final NiftySize availableSize) {
    NiftySize childSize = NiftySize.ZERO;
    for (NiftyNodeImpl<?> child : getLayout().getDirectChildren(this)) {
      childSize = NiftySize.max(getLayout().measure(child, availableSize), childSize);
    }
    return childSize;
  }

  @Override
  protected void arrangeInternal(@Nonnull final NiftyRect area) {
    for (NiftyNodeImpl<?> child : getLayout().getDirectChildren(this)) {
      getLayout().arrange(child, area);
    }
  }

  @Override
  protected AlignmentLayoutChildNode createNode() {
    return new AlignmentLayoutChildNode(this);
  }
}
