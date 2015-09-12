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

/**
 * Created by void on 12.09.15.
 */
public class NiftyReferenceNode implements NiftyNode {
  private final NiftyReferenceNodeImpl impl;

  public static NiftyReferenceNode referenceNode(final String id) {
    return new NiftyReferenceNode(id);
  }

  private NiftyReferenceNode(final String id) {
    this.impl = new NiftyReferenceNodeImpl(id);
  }

  NiftyReferenceNode(final NiftyReferenceNodeImpl impl) {
    this.impl = impl;
  }

  public String getId() {
    return impl.getId();
  }

  public String toString() {
    return "(" + this.getClass().getSimpleName() + ") " + impl.getId();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NiftyReferenceNode that = (NiftyReferenceNode) o;
    return !(impl != null ? !impl.equals(that.impl) : that.impl != null);
  }

  @Override
  public int hashCode() {
    return impl != null ? impl.hashCode() : 0;
  }

  // friend access

  NiftyReferenceNodeImpl getImpl() {
    return impl;
  }
}
