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
 * A rectangle marking a area at a specific location.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public final class NiftyRect {
  @Nonnull
  public static final NiftyRect INVALID = new NiftyRect(new NiftyPoint(0, 0), NiftySize.INVALID);

  @Nonnull
  private final NiftyPoint origin;

  @Nonnull
  private final NiftySize size;

  public NiftyRect(@Nonnull final NiftyPoint origin, @Nonnull final NiftySize size) {
    this.origin = origin;
    this.size = size;
  }

  @Nonnull
  public NiftyPoint getOrigin() {
    return origin;
  }

  @Nonnull
  public NiftySize getSize() {
    return size;
  }

  @Override
  public boolean equals(@Nullable final Object other) {
    return (other instanceof NiftyRect) && equals((NiftyRect) other);
  }

  public boolean equals(@Nullable final NiftyRect other) {
    return (other != null) && size.equals(other.size) && origin.equals(other.origin);
  }

  public boolean equals(@Nullable final NiftyRect other, final float tolerance) {
    return (other != null) && size.equals(other.size, tolerance) && origin.equals(other.origin, tolerance);
  }

  @Override
  public int hashCode() {
    int hash = 31;
    hash = hash * 27 + size.hashCode();
    hash = hash * 27 + origin.hashCode();
    return hash;
  }

  @Nonnull
  @Override
  public String toString() {
    return size + " at " + origin;
  }
}
