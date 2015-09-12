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

import de.lessvoid.nifty.node.NiftyReferenceNode;
import de.lessvoid.nifty.spi.node.NiftyNode;

/**
 * A fluent API to help building the NiftyNode structure.
 *
 * Created by void on 09.08.15.
 */
public class NiftyNodeBuilder {
  private final Nifty nifty;
  private final NiftyNode parent;
  private final NiftyNode node;

  /**
   * Nifty will return this instance for you. You're not supposed to construct it yourself.
   * @param nifty the Nifty instance
   * @param parent the parent node
   * @param node the node node just added
   */
  NiftyNodeBuilder(final Nifty nifty, final NiftyNode parent, final NiftyNode node) {
    this.nifty = nifty;
    this.parent = parent;
    this.node = node;
  }

  /**
   * Add a new top level child node to the root node.
   * @param child the new child to add
   * @return this containing the new child node
   */
  public NiftyNodeBuilder addTopLevelNode(final NiftyNode child) {
    return nifty.addNode(child);
  }

  /**
   * Add a new child node to the parent node given.
   * @param parent the parent to add the child node to
   * @param child the child node to add
   * @return this containing the new child node
   */
  public NiftyNodeBuilder addNode(final NiftyNode parent, final NiftyNode child) {
    return nifty.addNode(parent, child);
  }

  /**
   * Add a new child node as a child to this node.
   * @param child the new child node
   * @return this containing the new child node
   */
  public NiftyNodeBuilder addNode(final NiftyNode child) {
    return nifty.addNode(this.node, child);
  }

  /**
   * Add a new node to the parent node this NiftyNodeBuilder instance is linked to.
   * @param sibling the new sibling to add
   * @return this
   */
  public NiftyNodeBuilder addSibling(final NiftyNode sibling) {
    return nifty.addNode(parent, sibling);
  }

  /**
   * Find a NiftyReferenceNode for the given nodeId and a return a NiftyNodeBuilder to add nodes as child nodes to this
   * reference node.
   *
   * @param nodeId the referenceNode to lookup
   * @return a NiftyNodeBuilder
   */
  public NiftyNodeBuilder addAsChildOf(final String nodeId) {
    NiftyReferenceNode referenceNode = nifty.getNiftyReferenceNode(nodeId);
    return new NiftyNodeBuilder(nifty, nifty.getParent(referenceNode), referenceNode);
  }
}
