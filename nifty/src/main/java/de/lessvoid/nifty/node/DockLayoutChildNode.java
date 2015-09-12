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
import de.lessvoid.nifty.types.NiftyDock;

import javax.annotation.Nonnull;

/**
 * This is the child node for a dock layout that allows docking the node to a specific border of the parent node.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class DockLayoutChildNode implements NiftyNode {
  @Nonnull
  private final DockLayoutChildNodeImpl implementation;

  @Nonnull
  public static DockLayoutChildNode dockNorthLayoutNode() {
    return dockLayoutChildNode(NiftyDock.North);
  }

  @Nonnull
  public static DockLayoutChildNode dockSouthLayoutNode() {
    return dockLayoutChildNode(NiftyDock.South);
  }

  @Nonnull
  public static DockLayoutChildNode dockEastLayoutNode() {
    return dockLayoutChildNode(NiftyDock.East);
  }

  @Nonnull
  public static DockLayoutChildNode dockWestLayoutNode() {
    return dockLayoutChildNode(NiftyDock.West);
  }

  @Nonnull
  public static DockLayoutChildNode dockLayoutChildNode(@Nonnull final NiftyDock dock) {
    return new DockLayoutChildNode(dock);
  }

  private DockLayoutChildNode(@Nonnull final NiftyDock dock) {
    this(new DockLayoutChildNodeImpl(dock));
  }

  public void setDock(@Nonnull final NiftyDock dock) {
    implementation.setDock(dock);
  }

  @Nonnull
  public NiftyDock getDock() {
    return implementation.getDock();
  }

  DockLayoutChildNode(@Nonnull final DockLayoutChildNodeImpl implementation) {
    this.implementation = implementation;
  }

  @Nonnull
  NiftyNodeImpl<DockLayoutChildNode> getImpl() {
    return implementation;
  }

  @Nonnull
  @Override
  public String toString() {
    return "(DockLayoutChildNode) [" + getDock().name() + "]";
  }
}
