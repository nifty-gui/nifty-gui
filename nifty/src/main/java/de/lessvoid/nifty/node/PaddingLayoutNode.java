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
 * The padding layout nodes applies a padding to the inner layout nodes. Each direct inner layout node is subject to
 * the padding applied.
 * <p />
 * Each child node will be stretched to the full available size minus the padding. The desired size will be the
 * maximum size requested by the child nodes plus the padding.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class PaddingLayoutNode implements NiftyNode {
  @Nonnull
  private final PaddingLayoutNodeImpl implementation;

  @Nonnull
  public static PaddingLayoutNode paddingLayoutNode(final float allSides) {
    return paddingLayoutNode(allSides, allSides);
  }

  @Nonnull
  public static PaddingLayoutNode paddingLayoutNode(final float topBottom, final float rightLeft) {
    return paddingLayoutNode(topBottom, rightLeft, topBottom);
  }

  @Nonnull
  public static PaddingLayoutNode paddingLayoutNode(final float top, final float rightLeft, final float bottom) {
    return paddingLayoutNode(top, rightLeft, bottom, rightLeft);
  }

  @Nonnull
  public static PaddingLayoutNode paddingLayoutNode(final float top, final float right, final float bottom, final float left) {
    return new PaddingLayoutNode(top, right, bottom, left);
  }

  private PaddingLayoutNode(final float top, final float right, final float bottom, final float left) {
    this(new PaddingLayoutNodeImpl(top, right, bottom, left));
  }

  PaddingLayoutNode(@Nonnull final PaddingLayoutNodeImpl implementation) {
    this.implementation = implementation;
  }

  public float getLeft() {
    return implementation.getLeft();
  }

  public void setLeft(final float left) {
    implementation.setLeft(left);
  }

  public float getRight() {
    return implementation.getRight();
  }

  public void setRight(final float right) {
    implementation.setRight(right);
  }

  public float getTop() {
    return implementation.getTop();
  }

  public void setTop(final float top) {
    implementation.setTop(top);
  }

  public float getBottom() {
    return implementation.getBottom();
  }

  public void setBottom(final float bottom) {
    implementation.setBottom(bottom);
  }

  @Nonnull
  NiftyNodeImpl<PaddingLayoutNode> getImpl() {
    return implementation;
  }

  @Override
  public boolean equals(@Nullable final Object obj) {
    return (obj instanceof PaddingLayoutNode) && equals((PaddingLayoutNode) obj);
  }

  public boolean equals(@Nullable final PaddingLayoutNode node) {
    return (node != null) && node.implementation.equals(implementation);
  }

  @Override
  public int hashCode() {
    return implementation.hashCode();
  }

  @Nonnull
  @Override
  public String toString() {
    return "(PaddingLayoutNode) top [" + getTop() + "px] right [" + getRight() +
        "px] bottom [" + getBottom() + "px] left [" + getLeft() + "px]";
  }
}
