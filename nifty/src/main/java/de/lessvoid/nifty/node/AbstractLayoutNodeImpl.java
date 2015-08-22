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

import de.lessvoid.nifty.NiftyLayout;
import de.lessvoid.nifty.spi.node.NiftyLayoutNodeImpl;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.types.Rect;
import de.lessvoid.nifty.types.Size;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
abstract class AbstractLayoutNodeImpl<T extends NiftyNode> implements NiftyLayoutNodeImpl<T> {
  @Nullable
  private NiftyLayout layout;
  private boolean measureValid;
  private boolean arrangeValid;
  @Nullable
  private Size desiredSize;
  @Nullable
  private Rect arrangeRect;
  @Nullable
  private Reference<T> niftyNodeRef;

  protected AbstractLayoutNodeImpl() {}

  @Override
  public void onAttach(@Nonnull final NiftyLayout layout) {
    if (this.layout != null) {
      throw new IllegalStateException("This node was already activated.");
    }
    this.layout = layout;
    invalidateMeasure();
    invalidateArrange();
  }

  @Override
  public void onDetach(@Nonnull final NiftyLayout layout) {
    if (this.layout == null) {
      throw new IllegalStateException("This node was never attached.");
    }
    if (this.layout != layout) {
      throw new IllegalArgumentException("The node is attached, but it seems it is handled by a different instance of" +
          " the layout system.");
    }
    layout.reportRemoval(this);
    this.layout = null;
  }

  @Override
  public boolean isMeasureValid() {
    return measureValid;
  }

  @Override
  public boolean isArrangeValid() {
    return arrangeValid;
  }

  @Override
  public void invalidateMeasure() {
    if ((layout != null) && isMeasureValid()) {
      measureValid = false;
      layout.reportMeasureInvalid(this);
    }
  }

  @Override
  public void invalidateArrange() {
    if ((layout != null) && isArrangeValid()) {
      arrangeValid = false;
      layout.reportArrangeInvalid(this);
    }
  }

  @Nonnull
  @Override
  public Size getDesiredSize() {
    return (desiredSize != null) && isMeasureValid() ? desiredSize : Size.INVALID;
  }

  @Nonnull
  @Override
  public Rect getArrangedRect() {
    return (arrangeRect == null) ? Rect.INVALID : arrangeRect;
  }

  @Nonnull
  @Override
  public final Size measure(@Nonnull final Size availableSize) {
    if (availableSize.isInvalid()) {
      throw new IllegalArgumentException("Supplied size value for measure must not be invalid.");
    }

    Size size = measureInternal(availableSize);
    if (!size.equals(desiredSize)) {
      invalidateArrange();
    }
    desiredSize = size;
    measureValid = true;
    return size;
  }

  @Nonnull
  protected abstract Size measureInternal(@Nonnull Size availableSize);

  @Override
  public final void arrange(@Nonnull final Rect area) {
    arrangeInternal(area);
    arrangeRect = area;
    arrangeValid = true;
  }

  protected abstract void arrangeInternal(@Nonnull Rect area);

  @Nonnull
  protected final NiftyLayout getLayout() {
    if (layout == null) {
      throw new IllegalStateException("The layout node is not activated yet.");
    }
    return layout;
  }

  @Override
  public final T getNiftyNode() {
    Reference<T> ref = niftyNodeRef;
    T node = (ref == null) ? null : ref.get();
    if (node == null) {
      node = createNode();
      niftyNodeRef = new SoftReference<>(node);
    }
    return node;
  }

  protected abstract T createNode();
}
