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
import de.lessvoid.nifty.spi.node.NiftyNode;
import de.lessvoid.nifty.spi.node.NiftyNodeImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static de.lessvoid.niftyinternal.tree.NiftyTreeNodeControls.entireTree;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodeConverters.toNiftyNode;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodeConverters.toNodeImpl;
import static de.lessvoid.niftyinternal.tree.NiftyTreeNodePredicates.nodeImplAny;

/**
 * Created by void on 23.07.15.
 */
public class InternalNiftyTree {
  private final NiftyTreeNode root;
  private final Map<NiftyNodeImpl<? extends NiftyNode>, NiftyTreeNode> implLookup = new HashMap<>();

  public InternalNiftyTree(final NiftyNodeImpl<? extends NiftyNode> rootNode) {
    assertNotNull(rootNode);
    this.root = new NiftyTreeNode(rootNode);
    registerNode(rootNode, root);
  }

  @Nonnull
  public NiftyNodeImpl<? extends NiftyNode> getRootNode() {
    return root.getValue();
  }

  @SafeVarargs
  @Nonnull
  public final InternalNiftyTree addChild(
      final NiftyNodeImpl<? extends NiftyNode> parent,
      final NiftyNodeImpl<? extends NiftyNode> child,
      final NiftyNodeImpl<? extends NiftyNode>... additionalChildren) {
    addNodes(treeNodeFromImpl(parent), child, additionalChildren);
    return this;
  }

  public void remove(final NiftyNodeImpl<? extends NiftyNode> niftyNode) {
    NiftyTreeNode niftyTreeNode = treeNodeFromImpl(niftyNode);
    if (niftyTreeNode == root) {
      throw new NiftyRuntimeException("can't remove the root node");
    }
    niftyTreeNode.remove();
    unregisterNode(niftyNode);
  }

  /**
   * Returns child nodes in a depth first manner starting from the root node.
   *
   * @return the Iterable
   */
  @Nonnull
  public Iterable<NiftyNodeImpl<?>> childNodes() {
    return childNodes(nodeImplAny(), toNodeImpl(), entireTree());
  }

  /**
   * Returns child nodes in a depth first manner starting from the root node.
   *
   * @param predicate the NiftyTreeNodePredicate the nodes must comply too
   * @return the Iterable
   */
  @Nonnull
  public Iterable<NiftyNodeImpl<?>> childNodes(
      @Nonnull final NiftyTreeNodePredicate predicate) {
    return childNodes(predicate, toNodeImpl(), entireTree());
  }

  /**
   * Returns child nodes in a depth first manner starting from the root node.
   *
   * @param predicate the NiftyTreeNodePredicate the nodes must comply too
   * @param converter the NiftyTreeNodeConverter
   * @param <T> the actual type of the entries the Iterator returns
   * @return the Iterable
   */
  @Nonnull
  public <T> Iterable<T> childNodes(
      @Nonnull final NiftyTreeNodePredicate predicate,
      @Nonnull final NiftyTreeNodeConverter<T> converter) {
    return childNodes(predicate, converter, entireTree());
  }

  /**
   * Returns child nodes in a depth first manner starting from the root node.
   *
   * @param predicate the NiftyTreeNodePredicate the nodes must comply too
   * @param converter the NiftyTreeNodeConverter
   * @param control the controller that steers the iterator in the tree
   * @param <T> the actual type of the entries the Iterator returns
   * @return the Iterable
   */
  @Nonnull
  public <T> Iterable<T> childNodes(
      @Nonnull final NiftyTreeNodePredicate predicate,
      @Nonnull final NiftyTreeNodeConverter<T> converter,
      @Nonnull final NiftyTreeNodeControl control) {
    return childNodes(predicate, converter, control, root);
  }

  /**
   * Returns child nodes in a depth first manner starting from startNode.
   *
   * @param startNode the startNode
   * @return the Iterable
   */
  @Nonnull
  public Iterable<NiftyNodeImpl<?>> childNodes(
      @Nonnull final NiftyNodeImpl<? extends NiftyNode> startNode) {
    return childNodes(nodeImplAny(), toNodeImpl(), entireTree(), startNode);
  }

  /**
   * Returns child nodes in a depth first manner starting from startNode.
   *
   * @param predicate the NiftyTreeNodePredicate the nodes must comply too
   * @param startNode the startNode
   * @return the Iterable
   */
  @Nonnull
  public Iterable<NiftyNodeImpl<?>> childNodes(
      @Nonnull final NiftyTreeNodePredicate predicate,
      @Nonnull final NiftyNodeImpl<? extends NiftyNode> startNode) {
    return childNodes(predicate, toNodeImpl(), entireTree(), startNode);
  }

  /**
   * Returns child nodes in a depth first manner starting from startNode.
   *
   * @param predicate the NiftyTreeNodePredicate the nodes must comply too
   * @param converter the NiftyTreeNodeConverter
   * @param startNode the startNode
   * @param <T> the actual type of the entries the Iterator returns
   * @return the Iterable
   */
  @Nonnull
  public <T> Iterable<T> childNodes(
      @Nonnull final NiftyTreeNodePredicate predicate,
      @Nonnull final NiftyTreeNodeConverter<T> converter,
      @Nonnull final NiftyNodeImpl<? extends NiftyNode> startNode) {
    return childNodes(predicate, converter, entireTree(), startNode);
  }

  /**
   * Returns child nodes in a depth first manner starting from startNode.
   *
   * @param predicate the NiftyTreeNodePredicate the nodes must comply too
   * @param converter the NiftyTreeNodeConverter
   * @param control the controller that steers the iterator in the tree
   * @param startNode the startNode
   * @param <T> the actual type of the entries the Iterator returns
   * @return the Iterable
   */
  @Nonnull
  public <T> Iterable<T> childNodes(
      @Nonnull final NiftyTreeNodePredicate predicate,
      @Nonnull final NiftyTreeNodeConverter<T> converter,
      @Nonnull final NiftyTreeNodeControl control,
      @Nonnull final NiftyNodeImpl<? extends NiftyNode> startNode) {
    return childNodes(predicate, converter, control, treeNodeFromImpl(startNode));
  }

  @Nonnull
  private <T> Iterable<T> childNodes(
      @Nonnull final NiftyTreeNodePredicate predicate,
      @Nonnull final NiftyTreeNodeConverter<T> converter,
      @Nonnull final NiftyTreeNodeControl control,
      @Nonnull final NiftyTreeNode startTreeNode) {
    return makeIterable(startTreeNode.iterator(predicate, converter, control));
  }

  @Nullable
  public NiftyNodeImpl getParent(
      @Nonnull final NiftyNodeImpl<? extends NiftyNode> current) {
    return getParent(NiftyNodeImpl.class, current);
  }

  @Nullable
  public <X extends NiftyNodeImpl<?>> X getParent(
      @Nonnull final Class<X> clazz,
      @Nonnull final NiftyNodeImpl<?> startNode) {
    NiftyTreeNode currentTreeNode = treeNodeFromImpl(startNode).getParent();
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

  @SafeVarargs
  private final void addNodes(
      final NiftyTreeNode parentTreeNode,
      final NiftyNodeImpl<? extends NiftyNode> child,
      final NiftyNodeImpl<? extends NiftyNode>... additionalChildren) {
    addChild(parentTreeNode, child);
    for (int i=0; i<additionalChildren.length; i++) {
      addChild(parentTreeNode, additionalChildren[i]);
    }
  }

  private void registerNode(final NiftyNodeImpl<? extends NiftyNode> niftyNodeImpl, final NiftyTreeNode niftyTreeNode) {
    implLookup.put(niftyNodeImpl, niftyTreeNode);
  }

  private void unregisterNode(final NiftyNodeImpl<? extends NiftyNode> niftyNodeImpl) {
    implLookup.remove(niftyNodeImpl);
  }

  private NiftyTreeNode treeNodeFromImpl(final NiftyNodeImpl<? extends NiftyNode> niftyNode) {
    NiftyTreeNode niftyTreeNode = implLookup.get(niftyNode);
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
