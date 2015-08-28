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

import de.lessvoid.nifty.NiftyCanvasPainter;
import de.lessvoid.nifty.spi.node.NiftyNode;

/**
 * Created by void on 09.08.15.
 */
public class NiftyContentNode implements NiftyNode {
  private final NiftyContentNodeImpl impl;

  public static NiftyContentNode contentNode() {
    return new NiftyContentNode(1024, 768);
  }

  // FIXME this is a workaround til we have real layout in place
  public static NiftyContentNode contentNode(final int w, final int h) {
    return new NiftyContentNode(w, h);
  }

  private NiftyContentNode(final int w, final int h) {
    this.impl = new NiftyContentNodeImpl(w, h);
  }

  NiftyContentNode(final NiftyContentNodeImpl impl) {
    this.impl = impl;
  }

  public int getW() {
    return impl.getContentWidth();
  }

  public int getH() {
    return impl.getContentHeight();
  }

  public NiftyContentNode setCanvasPainter(final NiftyCanvasPainter niftyCanvasPainter) {
    impl.setCanvasPainter(niftyCanvasPainter);
    return this;
  }

  public String toString() {
    return "(" + this.getClass().getSimpleName() + ") FIXME";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NiftyContentNode that = (NiftyContentNode) o;
    return !(impl != null ? !impl.equals(that.impl) : that.impl != null);
  }

  @Override
  public int hashCode() {
    return impl != null ? impl.hashCode() : 0;
  }

  // friend access

  NiftyContentNodeImpl getImpl() {
    return impl;
  }
}
