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
package de.lessvoid.nifty.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * A size in 2D-Space defined by a width and a height value.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public final class NiftySize {
  public static final NiftySize INVALID = new NiftySize(Float.NaN, Float.NaN);
  public static final NiftySize INFINITE = new NiftySize(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
  public static final NiftySize ZERO = new NiftySize(0, 0);

  private final float width;
  private final float height;

  public NiftySize(final float width, final float height) {
    this.width = width;
    this.height = height;
  }

  @Nonnull
  public static NiftySize max(@Nonnull final NiftySize size1, @Nonnull final NiftySize size2) {
    if ((size1.width >= size2.width) && (size1.height >= size2.height)) {
      return size1;
    } else if ((size1.width <= size2.width) && (size1.height <= size2.height)) {
      return size2;
    } else {
      return new NiftySize(Math.max(size1.width, size2.width), Math.max(size1.height, size2.height));
    }
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public boolean isInvalid() {
    return Float.isNaN(width) || Float.isNaN(height);
  }

  public boolean isInfinite() {
    return Float.isInfinite(width) || Float.isInfinite(height);
  }

  @Override
  public boolean equals(@Nullable final Object other) {
    return (other instanceof NiftySize) && equals((NiftySize) other);
  }

  public boolean equals(@Nullable final NiftySize other) {
    return (other != null) &&
        (Float.floatToIntBits(width) == Float.floatToIntBits(other.width)) &&
        (Float.floatToIntBits(height) == Float.floatToIntBits(other.height));
  }

  public boolean equals(@Nullable final NiftySize other, final float tolerance) {
    return (other != null) &&
        (Math.abs(width - other.width) <= tolerance) &&
        (Math.abs(height - other.height) <= tolerance);
  }

  @Override
  public int hashCode() {
    int hash = 31;
    hash = hash * 27 + Float.floatToIntBits(width);
    hash = hash * 27 + Float.floatToIntBits(height);
    return hash;
  }

  @Nonnull
  @Override
  public String toString() {
    return width + " x " + height;
  }
}
