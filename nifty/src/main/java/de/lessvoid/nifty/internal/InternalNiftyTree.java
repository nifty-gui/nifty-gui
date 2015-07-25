package de.lessvoid.nifty.internal;

import de.lessvoid.nifty.api.NiftyRuntimeException;
import de.lessvoid.nifty.api.node.NiftyNode;
import de.lessvoid.nifty.internal.node.NiftyTreeNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by void on 23.07.15.
 */
public class InternalNiftyTree {
  private final NiftyTreeNode<NiftyNode> root;
  private final Map<NiftyNode, NiftyTreeNode<NiftyNode>> nodeLookup = new HashMap<>();

  public InternalNiftyTree(final NiftyNode rootNode) {
    assertNotNull(rootNode);
    this.root = new NiftyTreeNode<>(rootNode);
    registerNode(rootNode, root);
  }

  /**
   * Returns the root NiftyNode of this tree.
   * @return the root NiftyNode
   */
  @Nonnull
  public NiftyNode getRootNode() {
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
  public InternalNiftyTree addChild(final NiftyNode parent, final NiftyNode child, NiftyNode... additionalChilds) {
    addNodes(treeNode(parent), child, additionalChilds);
    return this;
  }

  /**
   * Remove the NiftyNode from the tree.
   *
   * @param niftyNode the NiftyNode to remove
   */
  public void remove(final NiftyNode niftyNode) {
    NiftyTreeNode<NiftyNode> niftyTreeNode = treeNode(niftyNode);
    if (niftyTreeNode == root) {
      throw new NiftyRuntimeException("can't remove the root node");
    }
    niftyTreeNode.remove();
    unregisterNode(niftyNode);
  }

  /**
   * Return a depth first Iterator for all NiftyNodes in this tree.
   * @return the Iterator
   */
  public Iterable<NiftyNode> childNodes() {
    return makeIterable(valueIterator(root));
  }

  /**
   * Return a depth first Iterator for all child nodes of the given parent node.
   * @return the Iterator
   */
  public Iterable<NiftyNode> childNodes(final NiftyNode startNode) {
    return makeIterable(valueIterator(treeNode(startNode)));
  }

  /**
   * Return a depth first Iterator for all NiftyNodes in this tree that are instances of the given class.
   * @param clazz only return entries if they are instances of this clazz
   * @return the Iterator
   */
  public <X extends NiftyNode> Iterable<X> filteredChildNodes(final Class<X> clazz) {
    return makeIterable(filteredValueIterator(clazz, root));
  }

  /**
   * Return a depth first Iterator for all child nodes of the given startNode.
   * @param clazz only return entries if they are instances of this clazz
   * @param startNode the start node
   * @return the Iterator
   */
  public <X extends NiftyNode> Iterable<X> filteredChildNodes(final Class<X> clazz, final NiftyNode startNode) {
    return makeIterable(filteredValueIterator(clazz, treeNode(startNode)));
  }

  /**
   * Get the parent NiftyNode of the niftyNode given.
   * @param current the NiftyNode in question
   * @return the parent NiftyNode or null
   */
  @Nullable
  public NiftyNode getParent(@Nonnull final NiftyNode current) {
    return getParent(NiftyNode.class, current);
  }

  @Nullable
  public <X extends NiftyNode> X getParent(@Nonnull final Class<X> clazz, @Nonnull final NiftyNode current) {
    NiftyTreeNode<NiftyNode> currentTreeNode = treeNode(current).getParent();
    while (currentTreeNode != null) {
      NiftyNode candidate = currentTreeNode.getValue();
      if (clazz.isAssignableFrom(candidate.getClass())) {
        //noinspection unchecked
        return (X) candidate;
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

  private void addNodes(final NiftyTreeNode<NiftyNode> parentTreeNode, final NiftyNode child, final NiftyNode... additionalChilds) {
    addChild(parentTreeNode, child);
    for (int i=0; i<additionalChilds.length; i++) {
      addChild(parentTreeNode, additionalChilds[i]);
    }
  }

  private Iterator<NiftyNode> valueIterator(final NiftyTreeNode<NiftyNode> startTreeNode) {
    return startTreeNode.valueIterator();
  }

  private <X> Iterator<X> filteredValueIterator(final Class<X> clazz, final NiftyTreeNode<NiftyNode> startTreeNode) {
    return startTreeNode.filteredChildIterator(clazz);
  }

  private void registerNode(final NiftyNode niftyNode, final NiftyTreeNode<NiftyNode> niftyTreeNode) {
    nodeLookup.put(niftyNode, niftyTreeNode);
  }

  private void unregisterNode(final NiftyNode niftyNode) {
    nodeLookup.remove(niftyNode);
  }

  private NiftyTreeNode<NiftyNode> treeNode(final NiftyNode niftyNode) {
    NiftyTreeNode<NiftyNode> niftyTreeNode = nodeLookup.get(niftyNode);
    if (niftyTreeNode == null) {
      throw new NiftyRuntimeException("could not find node [" + niftyNode + "]");
    }
    return niftyTreeNode;
  }

  private void addChild(final NiftyTreeNode parent, final NiftyNode child) {
    NiftyTreeNode<NiftyNode> childTreeNode = new NiftyTreeNode<>(child);
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

  private void assertNotNull(final NiftyNode rootNode) {
    if (rootNode == null) {
      throw new NiftyRuntimeException("rootNode must not be null when constructing tree");
    }
  }
}
