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
package de.lessvoid.nifty;

import de.lessvoid.nifty.spi.NiftyNode;

/**
 * A fluent API to help building the NiftyNode structure.
 *
 * Created by void on 09.08.15.
 */
public class NiftyNodeBuilder {
  private final Nifty nifty;
  private final NiftyNode child;

  /**
   * Nifty will return this instance for you. You're not supposed to construct it yourself.
   * @param nifty the Nifty instance
   * @param child the child node just added
   */
  NiftyNodeBuilder(final Nifty nifty, final NiftyNode child) {
    this.nifty = nifty;
    this.child = child;
  }

  /**
   * Add a new top level child node to the root node.
   * @param child the new child to add
   * @return this containing the new child node
   */
  public NiftyNodeBuilder node(final NiftyNode child) {
    return nifty.node(child);
  }

  /**
   * Add a new child node to the parent node given.
   * @param parent the parent to add the child node to
   * @param child the child node to add
   * @return this containing the new child node
   */
  public NiftyNodeBuilder node(final NiftyNode parent, final NiftyNode child) {
    return nifty.node(parent, child);
  }

  /**
   * Add a new child node as a child to this node.
   * @param child the new child node
   * @return this containing the new child node
   */
  public NiftyNodeBuilder childNode(final NiftyNode child) {
    return nifty.node(this.child, child);
  }
}
