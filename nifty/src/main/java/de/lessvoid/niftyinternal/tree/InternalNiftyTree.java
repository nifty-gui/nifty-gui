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
package de.lessvoid.niftyinternal.tree;

import de.lessvoid.nifty.NiftyRuntimeException;
import de.lessvoid.nifty.spi.NiftyNode;
import de.lessvoid.nifty.spi.NiftyNodeImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by void on 23.07.15.
 */
public class InternalNiftyTree {
  private final NiftyTreeNode root;
  private final Map<NiftyNodeImpl<? extends NiftyNode>, NiftyTreeNode> implLookup = new HashMap<>();
  private final Map<NiftyNode, NiftyTreeNode> nodeLookup = new HashMap<>();

  public InternalNiftyTree(final NiftyNodeImpl rootNode) {
    assertNotNull(rootNode);
    this.root = new NiftyTreeNode(rootNode);
    registerNode(rootNode, root);
  }

  /**
   * Returns the root NiftyNode of this tree.
   * @return the root NiftyNode
   */
  @Nonnull
  public NiftyNode getRootNode() {
    return root.getValue().getNiftyNode();
  }

  /**
   * Returns the root NiftyNode of this tree.
   * @return the root NiftyNode
   */
  @Nonnull
  public NiftyNodeImpl getRootNodeImpl() {
    return root.getValue();
  }

  /**
   * Add the given child(s) NiftyNode(s) to the given parent NiftyNode.
   *
   * @param parent the NiftyNode parent to add the child to
   * @param child the child NiftyNode to add to the parent
   * @param additionalChilds additional childNodes to add as well
   * @return this
   */
  public InternalNiftyTree addChild(final NiftyNodeImpl<? extends NiftyNode> parent, final NiftyNodeImpl<? extends NiftyNode> child, NiftyNodeImpl<? extends NiftyNode>... additionalChilds) {
    addNodes(treeNodeFromImpl(parent), child, additionalChilds);
    return this;
  }

  /**
   * Add the given child(s) NiftyNode(s) to the given parent NiftyNode.
   *
   * @param parent the NiftyNode parent to add the child to
   * @param child the child NiftyNode to add to the parent
   * @param additionalChilds additional childNodes to add as well
   * @return this
   */
  public InternalNiftyTree addChild(final NiftyNode parent, final NiftyNodeImpl<? extends NiftyNode> child, NiftyNodeImpl<? extends NiftyNode>... additionalChilds) {
    addNodes(treeNode(parent), child, additionalChilds);
    return this;
  }

  /**
   * Remove the NiftyNode from the tree.
   *
   * @param niftyNode the NiftyNode to remove
   */
  public void remove(final NiftyNodeImpl<? extends NiftyNode> niftyNode) {
    NiftyTreeNode niftyTreeNode = treeNodeFromImpl(niftyNode);
    if (niftyTreeNode == root) {
      throw new NiftyRuntimeException("can't remove the root node");
    }
    niftyTreeNode.remove();
    unregisterNode(niftyNode);
  }

  public void remove(final NiftyNode niftyNode) {
    NiftyTreeNode niftyTreeNode = nodeLookup.get(niftyNode);
    if (niftyTreeNode == null) {
      throw new NiftyRuntimeException("can't remove none-existent node");
    }
    remove(niftyTreeNode.getValue());
  }

  /**
   * Return a depth first Iterator for all NiftyNodes in this tree.
   * @return the Iterator
   */
  public Iterable<? extends NiftyNodeImpl> childNodes() {
    return makeIterable(niftyNodeImplIterator(root));
  }

  public Iterable<? extends NiftyNode> niftyChildNodes() {
    return makeIterable(niftyNodeIterator(root));
  }

  /**
   * Return a depth first Iterator for all child nodes of the given parent node.
   * @return the Iterator
   */
  public Iterable<? extends NiftyNodeImpl> childNodes(final NiftyNodeImpl startNode) {
    return makeIterable(niftyNodeImplIterator(treeNodeFromImpl(startNode)));
  }

  /**
   * Return a depth first Iterator for all child nodes of the given parent node.
   * @return the Iterator
   */
  public Iterable<? extends NiftyNodeImpl> childNodes(final NiftyNode startNode) {
    return makeIterable(niftyNodeImplIterator(treeNode(startNode)));
  }

  /**
   * Return a depth first Iterator for all NiftyNodes in this tree that are instances of the given class.
   * @param clazz only return entries if they are instances of this clazz
   * @return the Iterator
   */
  public <T extends NiftyNode> Iterable<T> filteredChildNodes(final Class<T> clazz) {
    return makeIterable(filteredNiftyNodeIterator(clazz, root));
  }

  /**
   * Return a depth first Iterator for all NiftyNodes in this tree that are instances of the given class.
   * @param clazz only return entries if they are instances of this clazz
   * @return the Iterator
   */
  public <T extends NiftyNodeImpl> Iterable<T> filteredChildNodesImpl(final Class<T> clazz) {
    return makeIterable(filteredNiftyNodeImplIterator(clazz, root));
  }

  /**
   * Return a depth first Iterator for all child nodes of the given startNode.
   * @param clazz only return entries if they are instances of this clazz
   * @param startNode the start node
   * @return the Iterator
   */
  public <T extends NiftyNode> Iterable<T> filteredChildNodes(final Class<T> clazz, final NiftyNodeImpl<?> startNode) {
    return makeIterable(filteredNiftyNodeIterator(clazz, treeNodeFromImpl(startNode)));
  }

  /**
   * Return a depth first Iterator for all child nodes of the given startNode.
   * @param clazz only return entries if they are instances of this clazz
   * @param startNode the start node
   * @return the Iterator
   */
  public <T extends NiftyNodeImpl> Iterable<T> filteredChildNodesImpl(final Class<T> clazz, final NiftyNodeImpl<?> startNode) {
    return makeIterable(filteredNiftyNodeImplIterator(clazz, treeNodeFromImpl(startNode)));
  }

  /**
   * Return a depth first Iterator for all child nodes of the given startNode.
   * @param clazz only return entries if they are instances of this clazz
   * @param startNode the start node
   * @return the Iterator
   */
  public <T extends NiftyNode> Iterable<T> filteredChildNodes(final Class<T> clazz, final NiftyNode startNode) {
    return makeIterable(filteredNiftyNodeIterator(clazz, treeNode(startNode)));
  }

  /**
   * Return a depth first Iterator for all child nodes of the given startNode.
   * @param clazz only return entries if they are instances of this clazz
   * @param startNode the start node
   * @return the Iterator
   */
  public <T extends NiftyNodeImpl> Iterable<T> filteredChildNodesImpl(final Class<T> clazz, final NiftyNode startNode) {
    return makeIterable(filteredNiftyNodeImplIterator(clazz, treeNode(startNode)));
  }

  /**
   * Return a depth first Iterator for all NiftyNodes in this tree that are instances of the given class.
   * @param clazz only return entries if they are instances of this clazz
   * @return the Iterator
   */
  public <T> Iterable<T> filteredChildNodesGeneral(final Class<T> clazz) {
    return makeIterable(filteredIterator(clazz, root));
  }

  /**
   * Get the parent NiftyNode of the niftyNode given.
   * @param current the NiftyNode in question
   * @return the parent NiftyNode or null
   */
  @Nullable
  public NiftyNodeImpl<? extends NiftyNode> getParent(@Nonnull final NiftyNodeImpl<? extends NiftyNode> current) {
    return getParent(NiftyNodeImpl.class, current);
  }

  @Nullable
  public <X extends NiftyNodeImpl<? extends NiftyNode>> X getParent(@Nonnull final Class<X> clazz, @Nonnull final NiftyNodeImpl<? extends NiftyNode> current) {
    NiftyTreeNode currentTreeNode = treeNodeFromImpl(current).getParent();
    while (currentTreeNode != null) {
      NiftyNodeImpl candidate = currentTreeNode.getValue();
      if (clazz.isInstance(candidate)) {
        return clazz.cast(candidate);
      }
      currentTreeNode = currentTreeNode.getParent();
    }
    return null;
  }

  @Nullable
  public <Y extends NiftyNodeImpl> Y getParentImpl(@Nonnull final Class<Y> clazz, @Nonnull final NiftyNodeImpl current) {
    NiftyTreeNode currentTreeNode = treeNodeFromImpl(current).getParent();
    while (currentTreeNode != null) {
      NiftyNodeImpl candidate = currentTreeNode.getValue();
      if (clazz.isInstance(candidate)) {
        return clazz.cast(candidate);
      }
      currentTreeNode = currentTreeNode.getParent();
    }
    return null;
  }

  @Override
  public String toString() {
    return root.toStringTree();
  }

  // Internals /////////////////////////////////////////////////////////////////////////////////////////////////////////

  private void addNodes(final NiftyTreeNode parentTreeNode, final NiftyNodeImpl<? extends NiftyNode> child, final NiftyNodeImpl<? extends NiftyNode> ... additionalChilds) {
    addChild(parentTreeNode, child);
    for (int i=0; i<additionalChilds.length; i++) {
      addChild(parentTreeNode, additionalChilds[i]);
    }
  }

  private Iterator<? extends NiftyNodeImpl> niftyNodeImplIterator(final NiftyTreeNode startTreeNode) {
    return startTreeNode.niftyNodeImplIterator();
  }

  private Iterator<? extends NiftyNode> niftyNodeIterator(final NiftyTreeNode startTreeNode) {
    return startTreeNode.niftyNodeIterator();
  }

  private <T extends NiftyNodeImpl> Iterator<T> filteredNiftyNodeImplIterator(final Class<T> clazz, final NiftyTreeNode startTreeNode) {
    return startTreeNode.filteredNiftyNodeImplIterator(clazz);
  }

  private <T extends NiftyNode> Iterator<T> filteredNiftyNodeIterator(final Class<T> clazz, final NiftyTreeNode startTreeNode) {
    return startTreeNode.filteredNiftyNodeIterator(clazz);
  }

  private <T> Iterator<T> filteredIterator(final Class<T> clazz, final NiftyTreeNode startTreeNode) {
    return startTreeNode.filteredIterator(clazz);
  }

  private void registerNode(final NiftyNodeImpl<? extends NiftyNode> niftyNodeImpl, final NiftyTreeNode niftyTreeNode) {
    implLookup.put(niftyNodeImpl, niftyTreeNode);
    nodeLookup.put(niftyNodeImpl.getNiftyNode(), niftyTreeNode);
  }

  private void unregisterNode(final NiftyNodeImpl<? extends NiftyNode> niftyNodeImpl) {
    implLookup.remove(niftyNodeImpl);
    nodeLookup.remove(niftyNodeImpl.getNiftyNode());
  }

  private NiftyTreeNode treeNodeFromImpl(final NiftyNodeImpl<? extends NiftyNode> niftyNode) {
    NiftyTreeNode niftyTreeNode = implLookup.get(niftyNode);
    if (niftyTreeNode == null) {
      throw new NiftyRuntimeException("could not find node [" + niftyNode + "]");
    }
    return niftyTreeNode;
  }

  private NiftyTreeNode treeNode(final NiftyNode niftyNode) {
    NiftyTreeNode niftyTreeNode = nodeLookup.get(niftyNode);
    if (niftyTreeNode == null) {
      throw new NiftyRuntimeException("could not find node [" + niftyNode + "]");
    }
    return niftyTreeNode;
  }

  private void addChild(final NiftyTreeNode parent, final NiftyNodeImpl<? extends NiftyNode> child) {
    NiftyTreeNode childTreeNode = new NiftyTreeNode(child);
    parent.addChild(childTreeNode);
    registerNode(child, childTreeNode);
  }

  private <X> Iterable<X> makeIterable(final Iterator<X> iterator) {
    return new Iterable<X>() {
      @Override
      public Iterator<X> iterator() {
        return iterator;
      }
    };
  }

  private void assertNotNull(final NiftyNodeImpl rootNode) {
    if (rootNode == null) {
      throw new NiftyRuntimeException("rootNode must not be null when constructing tree");
    }
  }
}
