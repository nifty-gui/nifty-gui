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
@SuppressWarnings("rawtypes")
public class TreeItemSelectedEvent implements NiftyEvent {
    
    private TreeBoxControl treeBoxControl;
    private TreeItem treeItem;

    public TreeItemSelectedEvent(TreeBoxControl treeBoxControl, TreeItem treeItem) {
        this.treeBoxControl = treeBoxControl;
        this.treeItem = treeItem;
    }

    public TreeItemSelectedEvent(TreeItem treeItem) {
        this.treeItem = treeItem;
    }

    public TreeBoxControl getTreeBoxControl() {
        return treeBoxControl;
    }

    public void setTreeBoxControl(TreeBoxControl treeBoxControl) {
        this.treeBoxControl = treeBoxControl;
    }

    public TreeItem getTreeItem() {
        return treeItem;
    }

    public void setTreeItem(TreeItem treeItem) {
        this.treeItem = treeItem;
    }
    
}
