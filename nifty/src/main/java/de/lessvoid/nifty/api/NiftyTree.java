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
package de.lessvoid.nifty.api;

import de.lessvoid.nifty.api.node.NiftyNode;
import de.lessvoid.nifty.internal.InternalNiftyTree;
import de.lessvoid.nifty.internal.accessor.NiftyTreeAccessor;

/**
 * Niftys scenegraph abstraction is this class. It contains methods to modify a hierachy of NiftyNodes.
 *
 * Created by void on 21.07.15.
 */
public class NiftyTree {
  private final InternalNiftyTree impl;

  private NiftyTree(final InternalNiftyTree impl) {
    this.impl = impl;
  }

  /**
   * Add the given child(s) NiftyNode(s) to the given parent NiftyNode.
   *
   * @param parent the NiftyNode parent to add the child to
   * @param child the child NiftyNode to add to the parent
   * @param additionalChilds additional childNodes to add as well
   * @return this
   */
  public NiftyTree addChild(final NiftyNode parent, final NiftyNode child, NiftyNode... additionalChilds) {
    impl.addChild(parent, child, additionalChilds);
    return this;
  }

  /**
   * Remove the NiftyNode from the tree.
   *
   * @param niftyNode the NiftyNode to remove
   */
  public void remove(final NiftyNode niftyNode) {
    impl.remove(niftyNode);
  }

  /**
   * Return a depth first Iterator for all NiftyNodes in this tree.
   * @return the Iterator
   */
  public Iterable<NiftyNode> childNodes() {
    return impl.childNodes();
  }

  /**
   * Return a depth first Iterator for all child nodes of the given parent node.
   * @return the Iterator
   */
  public Iterable<NiftyNode> childNodes(final NiftyNode startNode) {
    return impl.childNodes(startNode);
  }

  /**
   * Return a depth first Iterator for all NiftyNodes in this tree that are instances of the given class.
   * @param clazz only return entries if they are instances of this clazz
   * @return the Iterator
   */
  public <X extends NiftyNode> Iterable<X> filteredChildNodes(final Class<X> clazz) {
    return impl.filteredChildNodes(clazz);
  }

  /**
   * Return a depth first Iterator for all child nodes of the given startNode.
   * @param clazz only return entries if they are instances of this clazz
   * @param startNode the start node
   * @return the Iterator
   */
  public <X extends NiftyNode> Iterable<X> filteredChildNodes(final Class<X> clazz, final NiftyNode startNode) {
    return impl.filteredChildNodes(clazz, startNode);
  }

  @Override
  public String toString() {
    return impl.toString();
  }

  // Accessor related

  InternalNiftyTree getImpl() {
    return impl;
  }

  static {
    NiftyTreeAccessor.DEFAULT = new InternalNiftyTreeAccessorImpl();
  }

  static NiftyTree newInstance(final InternalNiftyTree impl) {
    return new NiftyTree(impl);
  }
}
