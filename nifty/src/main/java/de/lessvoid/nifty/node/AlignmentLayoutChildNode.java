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
 * This is a child layout node for the {@link AlignmentLayoutNode}.
 *
 * This node allows setting the horizontal and vertical alignment that is applied to this node. Aside from the
 * additional information applied, acts in the same way as a {@link SimpleLayoutNode}. So it just arranges any child
 * item to it's full available size. The size this node requests will match the maximum height and width of all child
 * nodes.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @see AlignmentLayoutNode
 */
public final class AlignmentLayoutChildNode implements NiftyNode {
  @Nonnull
  private final AlignmentLayoutChildNodeImpl implementation;

  /**
   * Create a new node that will be aligned in the top left corner of the parent node.
   * <p />
   * Equals: {@code alignmentLayoutChildNode(Horizontal.Left, Vertical.Top)}
   *
   * @return the new node
   */
  @Nonnull
  public static AlignmentLayoutChildNode alignmentLayoutChildNode() {
    return alignmentLayoutChildNode(Horizontal.Left, Vertical.Top);
  }

  /**
   * Create a new node with specific alignment values.
   *
   * @param horizontal the horizontal alignment
   * @param vertical the vertical alignment
   * @return the new node
   */
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

  /**
   * Get the horizontal alignment that is applied to this node.
   *
   * @return the horizontal alignment
   */
  @Nonnull
  public Horizontal getHorizontal() {
    return implementation.getHorizontal();
  }

  /**
   * Set the horizontal alignment that is applied to this node.
   * <p />
   * Changing this value will trigger the a update of the layout.
   *
   * @param horizontal the new alignment value
   */
  public void setHorizontal(@Nonnull final Horizontal horizontal) {
    implementation.setHorizontal(horizontal);
  }

  /**
   * Get the vertical alignment that is applied to this node.
   *
   * @return the vertical alignment
   */
  @Nonnull
  public Vertical getVertical() {
    return implementation.getVertical();
  }

  /**
   * Set the vertical alignment that is applied to this node.
   * <p />
   * Changing this value will trigger the a update of the layout.
   *
   * @param vertical the new alignment value
   */
  public void setVertical(@Nonnull final Vertical vertical) {
    implementation.setVertical(vertical);
  }

  @Nonnull
  NiftyNodeImpl<AlignmentLayoutChildNode> getImpl() {
    return implementation;
  }

  @Override
  public boolean equals(@Nullable final Object obj) {
    return (obj instanceof AlignmentLayoutChildNode) && equals((AlignmentLayoutChildNode) obj);
  }

  public boolean equals(@Nullable final AlignmentLayoutChildNode node) {
    return (node != null) && node.implementation.equals(implementation);
  }

  @Override
  public int hashCode() {
    return implementation.hashCode();
  }

  @Nonnull
  @Override
  public String toString() {
    return "(AlignmentLayoutChildNode) horizontal [" + getHorizontal().name() +
        "] vertical [" + getVertical().name()+ ']';
  }
}
