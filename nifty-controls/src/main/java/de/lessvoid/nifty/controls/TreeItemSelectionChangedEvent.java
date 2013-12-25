/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing
 */
@SuppressWarnings("rawtypes")
public class TreeItemSelectionChangedEvent<T> extends ListBoxSelectionChangedEvent<TreeItem<T>> {
  @Nonnull
  private final TreeBox treeBoxControl;

  public TreeItemSelectionChangedEvent(
      @Nonnull final TreeBox<T> treeBoxControl,
      @Nonnull final ListBoxSelectionChangedEvent<TreeItem<T>> org) {
    super(treeBoxControl, org.getSelection(), org.getSelectionIndices());
    this.treeBoxControl = treeBoxControl;
  }

  @Nonnull
  public TreeBox getTreeBoxControl() {
    return treeBoxControl;
  }
}
