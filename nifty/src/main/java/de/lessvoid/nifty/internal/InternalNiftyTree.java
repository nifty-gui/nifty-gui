package de.lessvoid.nifty.internal;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyRuntimeException;
import de.lessvoid.nifty.api.node.NiftyNode;
import de.lessvoid.nifty.internal.node.NiftyTreeNode;
import de.lessvoid.nifty.internal.node.NiftyTreeNodeClassFilterIterator;
import de.lessvoid.nifty.internal.node.NiftyTreeNodeDepthFirstIterator;
import de.lessvoid.nifty.internal.node.NiftyTreeNodeValueIterator;

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
    this.root = new NiftyTreeNode<>(rootNode);
  }

  /**
   * Add the given NiftyNode directly to the root node.
   *
   * @param child the new NiftyNode to add to the root node as a new childs
   * @param additionalChilds additional childs to add as well
   * @return the last NiftyNode added
   */
  public NiftyNode add(final NiftyNode child, NiftyNode ... additionalChilds) {
    return addNodes(child, root, additionalChilds);
  }

  /**
   * Add the given child NiftyNode to the parent NiftyNode.
   *
   * @param parent the NiftyNode parent to add the child to
   * @param child the child NiftyNode to add to the parent
   * @param additionalChilds additional childs to add as well
   * @return the last child NiftyNode just added
   */
  public NiftyNode add(final NiftyNode parent, final NiftyNode child, NiftyNode ... additionalChilds) {
    return addNodes(child, treeNode(parent), additionalChilds);
  }

  /**
   * Remove the NiftyNode from the tree.
   *
   * @param niftyNode the NiftyNode to remove
   */
  public void remove(final NiftyNode niftyNode) {
    treeNode(niftyNode).remove();
    unregisterNode(niftyNode);
  }

  /**
   * Return a depth first Iterator for all NiftyNodes in this tree.
   * @return the Iterator
   */
  public Iterator<NiftyNode> childIterator() {
    return valueIterator(root);
  }

  /**
   * Return a depth first Iterator for all child nodes of the given parent node.
   * @return the Iterator
   */
  public Iterator<NiftyNode> childIterator(final NiftyNode startNode) {
    NiftyTreeNode<NiftyNode> startTreeNode = treeNode(startNode);
    return valueIterator(startTreeNode);
  }

  /**
   * Return a depth first Iterator for all NiftyNodes in this tree that are instances of the given class.
   * @param clazz only return entries if they are instances of this clazz
   * @return the Iterator
   */
  public <X> Iterator<NiftyNode> filteredChildIterator(final Class<X> clazz) {
    return filteredValueIterator(clazz, root);
  }

  /**
   * Return a depth first Iterator for all child nodes of the given startNode.
   * @param clazz only return entries if they are instances of this clazz
   * @param startNode the start node
   * @return the Iterator
   */
  public <X> Iterator<NiftyNode> filteredChildIterator(final Class<X> clazz, final NiftyNode startNode) {
    return filteredValueIterator(clazz, treeNode(startNode));
  }

  // Internals /////////////////////////////////////////////////////////////////////////////////////////////////////////

  private NiftyNode addNodes(final NiftyNode child, final NiftyTreeNode<NiftyNode> parentTreeNode, final NiftyNode ... additionalChilds) {
    NiftyNode result = addChild(parentTreeNode, child);
    for (int i=0; i<additionalChilds.length; i++) {
      result = addChild(parentTreeNode, additionalChilds[i]);
    }
    return result;
  }

  private Iterator<NiftyNode> valueIterator(final NiftyTreeNode<NiftyNode> startTreeNode) {
    return startTreeNode.valueIterator();
  }

  private <X> Iterator<NiftyNode> filteredValueIterator(final Class<X> clazz, final NiftyTreeNode<NiftyNode> startTreeNode) {
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

  private NiftyNode addChild(final NiftyTreeNode parent, final NiftyNode child) {
    NiftyTreeNode<NiftyNode> childTreeNode = new NiftyTreeNode<>(child);
    parent.addChild(childTreeNode);
    registerNode(child, childTreeNode);
    return child;
  }
}
