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

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
final class SimpleLayoutNodeImpl extends AbstractLayoutNodeImpl<SimpleLayoutNode> {
  @Nonnull
  @Override
  protected NiftySize measureInternal(@Nonnull final NiftySize availableSize) {
    NiftySize result = NiftySize.ZERO;
    for (NiftyNodeImpl<?> child : getLayout().getDirectChildren(this)) {
      NiftySize childSize = getLayout().measure(child, availableSize);
      result = NiftySize.max(result, childSize);
    }
    return result;
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
  protected SimpleLayoutNode createNode() {
    return new SimpleLayoutNode(this);
  }
}
