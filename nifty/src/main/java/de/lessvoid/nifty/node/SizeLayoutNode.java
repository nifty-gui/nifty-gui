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
 * This layout node has a fixed size it applies. This node actually ignores the size requirements of all child nodes
 * and forces the set size. For the arrangement the size is not enforced. It will forward the arrangement size to the
 * child nodes unmodified.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SizeLayoutNode implements NiftyNode {
  @Nonnull
  private final SizeLayoutNodeImpl implementation;

  @Nonnull
  public static SizeLayoutNode fixedSizeLayoutNode() {
    return fixedSizeLayoutNode(NiftySize.ZERO);
  }

  @Nonnull
  public static SizeLayoutNode fixedSizeLayoutNode(final float width, final float height) {
    return fixedSizeLayoutNode(newNiftySize(width, height));
  }

  @Nonnull
  public static SizeLayoutNode fixedSizeLayoutNode(@Nonnull final NiftySize size) {
    return sizeLayoutNode(size, SizeLayoutNodeMode.Fixed);
  }

  @Nonnull
  public static SizeLayoutNode maximalSizeLayoutNode(final float width, final float height) {
    return maximalSizeLayoutNode(newNiftySize(width, height));
  }

  @Nonnull
  public static SizeLayoutNode maximalSizeLayoutNode(@Nonnull final NiftySize size) {
    return sizeLayoutNode(size, SizeLayoutNodeMode.Maximal);
  }

  @Nonnull
  public static SizeLayoutNode minimalSizeLayoutNode(final float width, final float height) {
    return minimalSizeLayoutNode(newNiftySize(width, height));
  }

  @Nonnull
  public static SizeLayoutNode minimalSizeLayoutNode(@Nonnull final NiftySize size) {
    return sizeLayoutNode(size, SizeLayoutNodeMode.Minimal);
  }

  @Nonnull
  public static SizeLayoutNode sizeLayoutNode(@Nonnull final NiftySize size,
                                              @Nonnull final SizeLayoutNodeMode mode) {
    return sizeLayoutNode(size, mode, mode);
  }

  @Nonnull
  public static SizeLayoutNode sizeLayoutNode(@Nonnull final NiftySize size,
                                              @Nonnull final SizeLayoutNodeMode widthMode,
                                              @Nonnull final SizeLayoutNodeMode heightMode) {
    return new SizeLayoutNode(size, widthMode, heightMode);
  }

  private SizeLayoutNode(@Nonnull final NiftySize size,
                         @Nonnull final SizeLayoutNodeMode widthMode, @Nonnull final SizeLayoutNodeMode heightMode) {
    this(new SizeLayoutNodeImpl(size, widthMode, heightMode));
  }

  SizeLayoutNode(@Nonnull final SizeLayoutNodeImpl implementation) {
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
  public SizeLayoutNodeMode getHeightMode() {
    return implementation.getHeightMode();
  }

  public void setHeightMode(@Nonnull final SizeLayoutNodeMode heightMode) {
    implementation.setHeightMode(heightMode);
  }

  @Nonnull
  public SizeLayoutNodeMode getWidthMode() {
    return implementation.getWidthMode();
  }

  public void setWidthMode(@Nonnull final SizeLayoutNodeMode widthMode) {
    implementation.setWidthMode(widthMode);
  }

  @Nonnull
  NiftyNodeImpl<SizeLayoutNode> getImpl() {
    return implementation;
  }

  @Nonnull
  @Override
  public String toString() {
    SizeLayoutNodeMode widthMode = getWidthMode();
    SizeLayoutNodeMode heightMode = getHeightMode();

    if (widthMode == heightMode) {
      switch (widthMode) {
        case Maximal:
          return "(MaximalSizeLayoutNode) width [" + getWidth() + "px] height [" + getHeight() + "px]";
        case Minimal:
          return "(MinimalSizeLayoutNode) width [" + getWidth() + "px] height [" + getHeight() + "px]";
        case Fixed:
          return "(FixedSizeLayoutNode) width [" + getWidth() + "px] height [" + getHeight() + "px]";
      }
    }

    StringBuilder builder = new StringBuilder();
    builder.append("(SizeLayoutNode) ");
    builder.append("width [").append(getWidth()).append("px");
    switch (widthMode) {
      case Maximal: builder.append("(max)"); break;
      case Minimal: builder.append("(min)"); break;
      case Fixed: break;
    }
    builder.append("] ");

    builder.append("heightMode [").append(getHeight()).append("px");
    switch (widthMode) {
      case Maximal: builder.append("(max)"); break;
      case Minimal: builder.append("(min)"); break;
      case Fixed: break;
    }
    builder.append(']');
    return builder.toString();
  }
}
