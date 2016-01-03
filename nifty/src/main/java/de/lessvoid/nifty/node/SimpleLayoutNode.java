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
import de.lessvoid.nifty.types.NiftySize;

import javax.annotation.Nonnull;

import static de.lessvoid.nifty.types.NiftySize.newNiftySize;

/**
 * This type of layout node has the maximal size of any of it's children as size requirement and forwards the
 * arrangement to all children in the same way.
 * <p />
 * This node is very handy to have a additional layer of layout nodes that are able to receive some layout data
 * before attaching additional nodes that receive the layout data.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class SimpleLayoutNode implements NiftyNode {
  @Nonnull
  private final SimpleLayoutNodeImpl implementation;

  @Nonnull
  public static SimpleLayoutNode simpleLayoutNode() {
    return new SimpleLayoutNode();
  }

  private SimpleLayoutNode() {
    this(new SimpleLayoutNodeImpl());
  }

  SimpleLayoutNode(@Nonnull final SimpleLayoutNodeImpl implementation) {
    this.implementation = implementation;
  }

  @Nonnull
  NiftyNodeImpl<SimpleLayoutNode> getImpl() {
    return implementation;
  }

  @Nonnull
  @Override
  public String toString() {
    return "(SimpleLayoutNode)";
  }
}
