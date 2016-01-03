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
import javax.annotation.Nullable;

/**
 * This is the layout node for a docking layout. This means that child nodes are docked to one of the four sides of
 * this layout. The order of the child nodes does matter, because it determines how much space is left for the node.
 * Also multiple nodes can be docked to one side to get a similar effect like a stack layout.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class DockLayoutNode implements NiftyNode {
  @Nonnull
  private final DockLayoutNodeImpl implementation;

  @Nonnull
  public static DockLayoutNode dockLayoutNode() {
    return dockLayoutNode(false);
  }

  @Nonnull
  public static DockLayoutNode dockLayoutNode(final boolean lastNodeFill) {
    return new DockLayoutNode(lastNodeFill);
  }

  private DockLayoutNode(final boolean lastNodeFill) {
    this(new DockLayoutNodeImpl(lastNodeFill));
  }

  public boolean isLastNodeFill() {
    return implementation.isLastNodeFill();
  }

  public void setLastNodeFill(final boolean lastNodeFill) {
    implementation.setLastNodeFill(lastNodeFill);
  }

  DockLayoutNode(@Nonnull final DockLayoutNodeImpl implementation) {
    this.implementation = implementation;
  }

  @Nonnull
  NiftyNodeImpl<DockLayoutNode> getImpl() {
    return implementation;
  }

  @Override
  public boolean equals(@Nullable final Object obj) {
    return (obj instanceof DockLayoutNode) && equals((DockLayoutNode) obj);
  }

  public boolean equals(@Nullable final DockLayoutNode node) {
    return (node != null) && node.implementation.equals(implementation);
  }

  @Override
  public int hashCode() {
    return implementation.hashCode();
  }

  @Nonnull
  @Override
  public String toString() {
    return "(DockLayoutNode)";
  }
}
