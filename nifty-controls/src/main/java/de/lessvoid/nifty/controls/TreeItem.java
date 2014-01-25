/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
  @Nonnull
  private final List<TreeItem<T>> children;

  /**
   * The parent of this item.
   */
  @Nullable
  private TreeItem<T> parentItem;

  /**
   * The current state of the item. This only applies in case the node is not the root node and not a leaf.
   */
  private boolean expanded;

  /**
   * The actual value inside this tree node.
   */
  @Nullable
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
  public TreeItem(@Nullable final T itemValue) {
    this();
    value = itemValue;
  }
  
  public void addTreeItem(final TreeItem<T> item) {
      children.add(item);
      item.setParentItem(this);
  }

  public void addTreeItems(@Nonnull final Collection<TreeItem<T>> items) {
      for (TreeItem<T> t : items)
          addTreeItem(t);
  }

  public void removeTreeItem(final TreeItem<T> item) {
      children.remove(item);
      item.setParentItem(null);
  }

  public void removeTreeItems(@Nonnull final Collection<TreeItem<T>> items) {
      for (TreeItem<T> t : items)
          removeTreeItem(t);
  }

  /**
   * Returns an iterator over a set of elements of type T.
   *
   * @return an Iterator.
   */
  @Nonnull
  @Override
  public Iterator<TreeItem<T>> iterator() {
    return children.iterator();
  }

  @Nullable
  public T getValue() {
    return value;
  }

  public void setValue(@Nullable final T value) {
    this.value = value;
  }

  @Nullable
  public TreeItem<T> getParentItem() {
    return parentItem;
  }

  public void setParentItem(@Nullable final TreeItem<T> parentItem) {
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
