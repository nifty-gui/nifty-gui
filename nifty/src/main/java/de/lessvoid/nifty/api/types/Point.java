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
package de.lessvoid.nifty.api.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * A point in 2D-Space defined by a X and a Y coordinate.
 *
 * @author void
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public final class Point {
  private final float x;
  private final float y;

  /**
   * @throws IllegalArgumentException Thrown in case {@code x} or {@code y} aren't finite values.
   */
  public Point(final float x, final float y) {
    // @mkaring: Float.isFinite is Java 8 which, unfortunately we can't support yet - how to replace that?
    //if (!Float.isFinite(x)) throw new IllegalArgumentException("x is expected to be a finite value.");
    //if (!Float.isFinite(y)) throw new IllegalArgumentException("y is expected to be a finite value.");

    this.x = x;
    this.y = y;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  @Override
  public boolean equals(@Nullable final Object other) {
    return (other instanceof Point) && equals((Point) other);
  }

  public boolean equals(@Nullable final Point other) {
    return (other != null) &&
        (Float.floatToIntBits(x) == Float.floatToIntBits(other.x)) &&
        (Float.floatToIntBits(y) == Float.floatToIntBits(other.y));
  }

  public boolean equals(@Nullable final Point other, final float tolerance) {
    return (other != null) &&
        (Math.abs(x - other.x) <= tolerance) &&
        (Math.abs(y - other.y) <= tolerance);
  }

  @Override
  public int hashCode() {
    int hash = 31;
    hash = hash * 27 + Float.floatToIntBits(x);
    hash = hash * 27 + Float.floatToIntBits(y);
    return hash;
  }

  @Nonnull
  @Override
  public String toString() {
    return x + ", " + y;
  }
}
