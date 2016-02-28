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

import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.types.NiftyLinearGradient;

/**
 * The API for the NiftyBackgroundFillNode. With this node you can change the background color state or the
 * background gradient state as well. A NiftyContentNode will use the current NiftyBackgroundFill state to fill the
 * content node.
 * <p>
 * If you specify both, NiftyColor and NiftyLinearGradient then the gradient override the background color.
 * <p>
 * Created by void on 09.08.15.
 */
public final class NiftyBackgroundFillNode implements NiftyNode {
  private NiftyBackgroundFillNodeImpl impl;

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Public API
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static NiftyBackgroundFillNode backgroundFillColor(final NiftyColor backgroundColor) {
    return new NiftyBackgroundFillNode(backgroundColor);
  }

  public static NiftyBackgroundFillNode backgroundFillGradient(final NiftyLinearGradient backgroundGradient) {
    return new NiftyBackgroundFillNode(backgroundGradient);
  }

  public final void setBackgroundColor(final NiftyColor backgroundColor) {
    impl.setBackgroundColor(backgroundColor);
  }

  public final NiftyColor getBackgroundColor() {
    return impl.getBackgroundColor();
  }

  public final void setBackgroundGradient(final NiftyLinearGradient backgroundGradient) {
    impl.setBackgroundGradient(backgroundGradient);
  }

  public final NiftyLinearGradient getBackgroundGradient() {
    return impl.getBackgroundGradient();
  }

  public final String toString() {
    return
      "(" + this.getClass().getSimpleName() + ") " +
        "backgroundGradient [" + impl.getBackgroundGradient() + "] " +
        "backgroundColor [" + impl.getBackgroundColor() + "]";
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NiftyBackgroundFillNode that = (NiftyBackgroundFillNode) o;
    return !(impl != null ? !impl.equals(that.impl) : that.impl != null);
  }

  @Override
  public final int hashCode() {
    return impl != null ? impl.hashCode() : 0;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Friend access
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  NiftyBackgroundFillNode(final NiftyBackgroundFillNodeImpl impl) {
    this.impl = impl;
  }

  NiftyBackgroundFillNodeImpl getImpl() {
    return impl;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Private API
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private NiftyBackgroundFillNode(final NiftyColor backgroundColor) {
    impl = new NiftyBackgroundFillNodeImpl(backgroundColor);
  }

  private NiftyBackgroundFillNode(final NiftyLinearGradient backgroundGradient) {
    impl = new NiftyBackgroundFillNodeImpl(backgroundGradient);
  }
}
