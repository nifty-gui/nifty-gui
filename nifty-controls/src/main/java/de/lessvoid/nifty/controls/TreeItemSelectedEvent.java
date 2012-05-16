/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.controls.treebox.TreeBoxControl;

/**
 *
 * @author ractoc
 */
public class TreeItemSelectedEvent<T> implements NiftyEvent {
    
    private TreeBoxControl<T> treeBoxControl;
    private TreeItem<T> treeItem;

    public TreeItemSelectedEvent(TreeBoxControl<T> treeBoxControl, TreeItem<T> treeItem) {
        this.treeBoxControl = treeBoxControl;
        this.treeItem = treeItem;
    }

    public TreeItemSelectedEvent(TreeItem<T> treeItem) {
        this.treeItem = treeItem;
    }

    public TreeBoxControl<T> getTreeBoxControl() {
        return treeBoxControl;
    }

    public void setTreeBoxControl(TreeBoxControl<T> treeBoxControl) {
        this.treeBoxControl = treeBoxControl;
    }

    public TreeItem<T> getTreeItem() {
        return treeItem;
    }

    public void setTreeItem(TreeItem<T> treeItem) {
        this.treeItem = treeItem;
    }
    
}
