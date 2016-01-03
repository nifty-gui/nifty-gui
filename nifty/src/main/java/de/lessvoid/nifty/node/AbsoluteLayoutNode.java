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
 * This is a layout node for a absolute value. It will place every single child node at 0,0 unless it is a
 * {@link AbsoluteLayoutChildNode} that supports setting a location.
 * <p>
 *   This layout will arrange it's children based on their desired size. So a children are <b>not</b> stretched to the
 *   full available size.
 * </p>
 * <h3>Layout Concept</h3>
 * <p>
 *   <img src="doc-files/AbsoluteLayoutNode-1.svg" alt="Concept Image" />
 * </p>
 * <ul>
 *   <li><b>X:</b> {@link AbsoluteLayoutChildNode#getX()}</li>
 *   <li><b>Y:</b> {@link AbsoluteLayoutChildNode#getY()}</li>
 * </ul>
 * <p>
 *   This node does not ensure that the child nodes are fully within the parent area. In case the X and Y values are
 *   chosen badly, the objects will be placed outside of the area of this node.
 * </p>
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class AbsoluteLayoutNode implements NiftyNode {
  @Nonnull
  private final AbsoluteLayoutNodeImpl implementation;

  /**
   * Create a new instance of the absolute layout node.
   * <p />
   * There are no parameters to this node, because the actual placement of the nodes is defined by x and y
   * coordinates defined in the {@link AbsoluteLayoutChildNode}s.
   *
   * @return the new instance
   */
  @Nonnull
  public static AbsoluteLayoutNode absoluteLayoutNode() {
    return new AbsoluteLayoutNode();
  }

  private AbsoluteLayoutNode() {
    this(new AbsoluteLayoutNodeImpl());
  }

  AbsoluteLayoutNode(@Nonnull final AbsoluteLayoutNodeImpl implementation) {
    this.implementation = implementation;
  }

  @Nonnull
  NiftyNodeImpl<AbsoluteLayoutNode> getImpl() {
    return implementation;
  }

  @Override
  public boolean equals(@Nullable  final Object obj) {
    return (obj instanceof AbsoluteLayoutNode) && ((AbsoluteLayoutNode) obj).implementation.equals(implementation);
  }

  @Override
  public int hashCode() {
    return implementation.hashCode();
  }

  @Nonnull
  @Override
  public String toString() {
    return "(AbsoluteLayoutNode)";
  }
}
