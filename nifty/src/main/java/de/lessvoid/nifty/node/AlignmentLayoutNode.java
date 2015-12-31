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

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * This is the parent node for a alignment layout. It requires its children to be {@link AlignmentLayoutChildNode}.
 * If a child has a different class, it will stretch it to the full size. This layout will align the nodes according
 * to the parameter set in the children. The node will not check for any overlapping. If you assign two objects to
 * the same alignment they will overlap.
 * <p />
 * This also means that this node will request the size of the largest child node as desired size.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class AlignmentLayoutNode implements NiftyNode {
  @Nonnull
  private final AlignmentLayoutNodeImpl implementation;

  @Nonnull
  public static AlignmentLayoutNode alignmentLayoutNode() {
    return new AlignmentLayoutNode();
  }

  private AlignmentLayoutNode() {
    this(new AlignmentLayoutNodeImpl());
  }

  AlignmentLayoutNode(@Nonnull final AlignmentLayoutNodeImpl implementation) {
    this.implementation = implementation;
  }

  @Nonnull
  NiftyNodeImpl<AlignmentLayoutNode> getImpl() {
    return implementation;
  }
}
