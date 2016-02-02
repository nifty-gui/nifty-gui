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
import de.lessvoid.nifty.types.NiftyPoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.lessvoid.nifty.types.NiftyPoint.newNiftyPoint;

/**
 * This is a child layout node for the {@link AbsoluteLayoutNode}.
 *
 * This node allows to set the x and y coordinate where the node is placed. Aside from the additional information
 * applied, acts in the same way as a {@link SimpleLayoutNode}. So it just arranges any child item to it's full
 * available size. The size this node requests will match the maximum height and width of all child nodes.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 * @see AbsoluteLayoutNode
 */
public final class AbsoluteLayoutChildNode implements NiftyNode {
  @Nonnull
  private final AbsoluteLayoutChildNodeImpl implementation;

  /**
   * Create a new node that will be placed at the coordinates specified by the {@code point} parameter.
   *
   * @param point the point that defines the location where the node is placed
   * @return the newly created node
   */
  @Nonnull
  public static AbsoluteLayoutChildNode absoluteLayoutChildNode(@Nonnull final NiftyPoint point) {
    return new AbsoluteLayoutChildNode(point);
  }

  /**
   * Create a new node that will be placed at the coordinates specified by the {@code x} and {@code y} parameters.
   * <p />
   * This function creates a instanced of {@link NiftyPoint} to store the coordinates. The parameters are subject to the
   * limitations set by this class.
   *
   * @param x the x coordinate of the point
   * @param y the y coordinate of the point
   * @return the newly created node
   * @throws IllegalArgumentException If one or both parameters are not finite values.
   * @see NiftyPoint
   */
  @Nonnull
  public static AbsoluteLayoutChildNode absoluteLayoutChildNode(final float x, final float y) {
    return absoluteLayoutChildNode(newNiftyPoint(x, y));
  }

  /**
   * Create a new node that will be placed at {@code 0 0}.
   *
   * @return The newly created node
   */
  @Nonnull
  public static AbsoluteLayoutChildNode absoluteLayoutChildNode() {
    return absoluteLayoutChildNode(0.f, 0.f);
  }

  private AbsoluteLayoutChildNode(@Nonnull final NiftyPoint point) {
    this(new AbsoluteLayoutChildNodeImpl(point));
  }

  AbsoluteLayoutChildNode(@Nonnull final AbsoluteLayoutChildNodeImpl implementation) {
    this.implementation = implementation;
  }

  /**
   * Get the X coordinate this node will be placed at.
   *
   * @return the x coordinate
   */
  public float getX() {
    return getPoint().getX();
  }

  /**
   * Change the X coordinate this node will be placed at.
   * <p />
   * Changing this value will trigger the a update of the layout.
   *
   * @param x the new x coordinate
   */
  public void setX(final float x) {
    setPoint(newNiftyPoint(x, getY()));
  }

  /**
   * Get the Y coordinate this node will be placed at.
   *
   * @return the y coordinate
   */
  public float getY() {
    return getPoint().getY();
  }

  /**
   * Change the Y coordinate this node will be placed at.
   * <p />
   * Changing this value will trigger the a update of the layout.
   *
   * @param y the new y coordinate
   */
  public void setY(final float y) {
    setPoint(newNiftyPoint(getX(), y));
  }

  @Nonnull
  public NiftyPoint getPoint() {
    return implementation.getPoint();
  }

  public void setPoint(@Nonnull final NiftyPoint point) {
    implementation.setPoint(point);
  }

  @Nonnull
  NiftyNodeImpl<AbsoluteLayoutChildNode> getImpl() {
    return implementation;
  }

  @Override
  public boolean equals(@Nullable final Object obj) {
    return (obj instanceof AbsoluteLayoutChildNode) && equals((AbsoluteLayoutChildNode) obj);
  }

  public boolean equals(@Nullable final AbsoluteLayoutChildNode node) {
    return (node != null) && node.implementation.equals(implementation);
  }

  @Override
  public int hashCode() {
    return implementation.hashCode();
  }

  @Nonnull
  @Override
  public String toString() {
    return "(AbsoluteLayoutChildNode) x [" + getX() + "px] y [" + getY() + "px]" ;
  }
}
