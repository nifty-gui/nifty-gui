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

import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;

/**
 * This is the child node for {@link AlignmentLayoutNode}. It allows setting the alignment that is supposed to be
 * applied to this node. In both orientations there is a special "stretch" alignment that will expand the node to the
 * full size of the parent. All other alignment options will honor the desired sizes of the children.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class AlignmentLayoutChildNode implements NiftyNode {
  @Nonnull
  private final AlignmentLayoutChildNodeImpl implementation;

  @Nonnull
  public static AlignmentLayoutChildNode alignmentLayoutChildNode(
      @Nonnull final Horizontal horizontal, @Nonnull final Vertical vertical) {
    return new AlignmentLayoutChildNode(horizontal, vertical);
  }

  private AlignmentLayoutChildNode(@Nonnull final Horizontal horizontal, @Nonnull final Vertical vertical) {
    this(new AlignmentLayoutChildNodeImpl(horizontal, vertical));
  }

  AlignmentLayoutChildNode(@Nonnull final AlignmentLayoutChildNodeImpl implementation) {
    this.implementation = implementation;
  }

  @Nonnull
  public Horizontal getHorizontal() {
    return implementation.getHorizontal();
  }

  public void setHorizontal(@Nonnull final Horizontal horizontal) {
    implementation.setHorizontal(horizontal);
  }

  @Nonnull
  public Vertical getVertical() {
    return implementation.getVertical();
  }

  public void setVertical(@Nonnull final Vertical vertical) {
    implementation.setVertical(vertical);
  }

  @Nonnull
  NiftyNodeImpl<AlignmentLayoutChildNode> getImpl() {
    return implementation;
  }
}
