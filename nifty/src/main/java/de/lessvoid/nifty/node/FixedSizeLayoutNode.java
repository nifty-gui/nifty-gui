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
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;

import static de.lessvoid.nifty.types.NiftySize.newNiftySize;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class FixedSizeLayoutNode implements NiftyNode {
  @Nonnull
  private final FixedSizeLayoutNodeImpl implementation;

  @Nonnull
  public static FixedSizeLayoutNode fixedSizeLayoutNode() {
    return fixedSizeLayoutNode(NiftySize.ZERO);
  }

  @Nonnull
  public static FixedSizeLayoutNode fixedSizeLayoutNode(final float width, final float height) {
    return fixedSizeLayoutNode(newNiftySize(width, height));
  }

  @Nonnull
  public static FixedSizeLayoutNode fixedSizeLayoutNode(@Nonnull final NiftySize size) {
    return new FixedSizeLayoutNode(size);
  }

  private FixedSizeLayoutNode(@Nonnull final NiftySize size) {
    this(new FixedSizeLayoutNodeImpl(size));
  }

  FixedSizeLayoutNode(@Nonnull final FixedSizeLayoutNodeImpl implementation) {
    this.implementation = implementation;
  }

  public float getWidth() {
    return getSize().getWidth();
  }

  public void setWidth(final float width) {
    setSize(newNiftySize(width, getHeight()));
  }

  public float getHeight() {
    return getSize().getHeight();
  }

  public void setHeight(final float height) {
    setSize(newNiftySize(getWidth(), height));
  }

  public void setSize(@Nonnull final NiftySize size) {
    implementation.setSize(size);
  }

  @Nonnull
  public NiftySize getSize() {
    return implementation.getSize();
  }

  @Nonnull
  NiftyNodeImpl<FixedSizeLayoutNode> getImpl() {
    return implementation;
  }

  @Nonnull
  @Override
  public String toString() {
    return "(FixedSizeLayoutNode) width [" + getWidth() + "px] height [" + getHeight() + "px]";
  }
}
