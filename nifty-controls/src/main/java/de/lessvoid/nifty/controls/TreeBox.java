/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import javax.annotation.Nonnull;

/**
 * This is the interface of the tree box control. Basically is a {@link ListBox} with some additions to display a tree.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface TreeBox<T> extends ListBox<TreeItem<T>> {
  /**
   * Set the tree that is supposed to be displayed.
   * <p/>
   * The root node of the tree will <b>NOT</b> be displayed. Only the children of this node are visible.
   *
   * @param treeRoot the root node of the tree
   */
  void setTree(@Nonnull TreeItem<T> treeRoot);
}
