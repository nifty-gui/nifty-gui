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
import java.util.Collection;

import static de.lessvoid.nifty.node.SizeLayoutNodeMode.Fixed;
import static de.lessvoid.nifty.node.SizeLayoutNodeMode.Maximal;
import static de.lessvoid.nifty.node.SizeLayoutNodeMode.Minimal;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class SizeLayoutNodeImpl extends AbstractLayoutNodeImpl<SizeLayoutNode> {
  @Nonnull
  private NiftySize size;
  @Nonnull
  private SizeLayoutNodeMode widthMode;
  @Nonnull
  private SizeLayoutNodeMode heightMode;

  public SizeLayoutNodeImpl(@Nonnull final NiftySize size,
                            @Nonnull final SizeLayoutNodeMode widthMode, @Nonnull final SizeLayoutNodeMode heightMode) {
    if (size.isInfinite() || size.isInvalid()) throw new IllegalArgumentException("The size has to be a finite value.");

    this.size = size;
    this.widthMode = widthMode;
    this.heightMode = heightMode;
  }

  @Nonnull
  public NiftySize getSize() {
    return size;
  }

  public void setSize(@Nonnull final NiftySize size) {
    if (size.isInfinite() || size.isInvalid()) throw new IllegalArgumentException("The size has to be a finite value.");

    if (!this.size.equals(size)) {
      this.size = size;
      invalidateMeasure();
    }
  }

  @Nonnull
  public SizeLayoutNodeMode getHeightMode() {
    return heightMode;
  }

  public void setHeightMode(@Nonnull final SizeLayoutNodeMode heightMode) {
    this.heightMode = heightMode;
  }

  @Nonnull
  public SizeLayoutNodeMode getWidthMode() {
    return widthMode;
  }

  public void setWidthMode(@Nonnull final SizeLayoutNodeMode widthMode) {
    this.widthMode = widthMode;
  }

  @Nonnull
  @Override
  protected NiftySize measureInternal(@Nonnull final NiftySize availableSize) {
    SizeLayoutNodeMode widthMode = getWidthMode();
    SizeLayoutNodeMode heightMode = getHeightMode();

    NiftySize size = this.size;
    NiftySize maxChildSize = NiftySize.ZERO;

    for (NiftyNodeImpl<?> child : getLayout().getDirectChildren(this)) {
      NiftySize childSize = getLayout().measure(child, size);
      maxChildSize = NiftySize.max(childSize, maxChildSize);
    }

    if ((widthMode == Fixed) && (heightMode == Fixed)) {
      return size;
    } else if ((widthMode == Maximal) && (heightMode == Maximal)) {
      return NiftySize.min(size, maxChildSize);
    } else if ((widthMode == Minimal) && (heightMode == Minimal)) {
      return NiftySize.min(size, maxChildSize);
    }

    float usedHeight;
    float usedWidth;
    switch (widthMode) {
      case Fixed: usedWidth = size.getWidth(); break;
      case Maximal: usedWidth = Math.min(size.getWidth(), maxChildSize.getWidth()); break;
      case Minimal: usedWidth = Math.max(size.getWidth(), maxChildSize.getWidth()); break;
      default: throw new UnsupportedOperationException("Unreachable");
    }
    switch (heightMode) {
      case Fixed: usedHeight = size.getHeight(); break;
      case Maximal: usedHeight = Math.min(size.getHeight(), maxChildSize.getHeight()); break;
      case Minimal: usedHeight = Math.max(size.getHeight(), maxChildSize.getHeight()); break;
      default: throw new UnsupportedOperationException("Unreachable");
    }

    if (size.equals(usedWidth, usedHeight)) {
      return size;
    } else if (maxChildSize.equals(usedWidth, usedHeight)) {
      return maxChildSize;
    }
    return NiftySize.newNiftySize(usedWidth, usedHeight);
  }

  @Override
  protected void arrangeInternal(@Nonnull final NiftyRect area) {
    Collection<NiftyNodeImpl<?>> children = getLayout().getDirectChildren(this);
    if (children.isEmpty()) {
      /* No child elements -> We are all done. */
      return;
    }

    for (NiftyNodeImpl<?> child : children) {
      getLayout().arrange(child, area);
    }
  }

  @Override
  protected SizeLayoutNode createNode() {
    return new SizeLayoutNode(this);
  }
}
