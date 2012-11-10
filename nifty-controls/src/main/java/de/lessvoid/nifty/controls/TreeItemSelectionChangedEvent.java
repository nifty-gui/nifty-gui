/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.controls.treebox.TreeBoxControl;

/**
 *
 * @author Martin Karing
 */
@SuppressWarnings("rawtypes")
public class TreeItemSelectionChangedEvent<T> extends ListBoxSelectionChangedEvent<TreeItem<T>> {
    
    private TreeBox treeBoxControl;

    public TreeItemSelectionChangedEvent(final TreeBoxControl<T> treeBoxControl,
                                 final ListBoxSelectionChangedEvent<TreeItem<T>> org) {
      super(treeBoxControl, org.getSelection(), org.getSelectionIndices());
      this.treeBoxControl = treeBoxControl;
    }

    public TreeBox<T> getTreeBoxControl() {
        return treeBoxControl;
    }
}
