/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.render.NiftyImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents any element in a tree list.
 *
 * @param <T> the type that is displayed in the node
 */
public final class TreeItem<T> implements Iterable<TreeItem<T>> {
  /**
   * The children of this tree item.
   */
  private final List<TreeItem<T>> children;

  /**
   * The parent of this item.
   */
  private TreeItem<T> parentItem;

  /**
   * The current state of the item. This only applies in case the node is not the root node and not a leaf.
   */
  private boolean expanded;

  /**
   * The actual value inside this tree node.
   */
  private T value;

  /**
   * The value how much the tree item is indented. This value is altered by the tree box control.
   */
  private int indent;

  /**
   * Default item for a constructor.
   */
  public TreeItem() {
    children = new ArrayList<TreeItem<T>>();
  }

  /**
   * Default item for a constructor.
   */
  public TreeItem(final T itemValue) {
    this();
    value = itemValue;
  }

  public void addTreeItem(final TreeItem<T> item) {
    children.add(item);
  }

  public void addTreeItems(final Collection<TreeItem<T>> items) {
    children.addAll(items);
  }

  /**
   * Returns an iterator over a set of elements of type T.
   *
   * @return an Iterator.
   */
  @Override
  public Iterator<TreeItem<T>> iterator() {
    return children.iterator();
  }

  public T getValue() {
    return value;
  }

  public void setValue(final T value) {
    this.value = value;
  }

  public TreeItem<T> getParentItem() {
    return parentItem;
  }

  public void setParentItem(final TreeItem<T> parentItem) {
    this.parentItem = parentItem;
  }

  public boolean isExpanded() {
    return expanded;
  }

  public void setExpanded(final boolean expanded) {
    this.expanded = expanded;
  }

  /**
   * Check if this tree item is a leaf. So if it does not have any children.
   *
   * @return {@code true} in case this tree item is a leaf
   */
  public boolean isLeaf() {
    return children.isEmpty();
  }

  public int getIndent() {
    return indent;
  }

  public void setIndent(final int indent) {
    this.indent = indent;
  }
}
