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
import de.lessvoid.nifty.types.NiftyRect;
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class FixedSizeLayoutNodeImpl extends AbstractLayoutNodeImpl<FixedSizeLayoutNode> {
  @Nonnull
  private NiftySize size;

  public FixedSizeLayoutNodeImpl(@Nonnull final NiftySize size) {
    if (size.isInfinite() || size.isInvalid()) throw new IllegalArgumentException("The size has to be a finite value.");

    this.size = size;
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
  @Override
  protected NiftySize measureInternal(@Nonnull final NiftySize availableSize) {
    /* Even if the measuring data of the children is not required, the children still need to be measured to ensure
     * that their size data is up to date. */
    for (NiftyNodeImpl<?> child : getLayout().getDirectChildren(this)) {
      getLayout().measure(child, size);
    }
    return size;
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
  protected FixedSizeLayoutNode createNode() {
    return new FixedSizeLayoutNode(this);
  }
}
