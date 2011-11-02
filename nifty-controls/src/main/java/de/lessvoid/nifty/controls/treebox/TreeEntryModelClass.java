/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.treebox;

import de.lessvoid.nifty.controls.TreeItem;

/**
 *
 * @author ractoc
 */
public class TreeEntryModelClass {
   
    private TreeItem treeItem;
    
    private int indent;
    
    public TreeEntryModelClass(int indent, TreeItem treeItem) {
        this.indent = indent;
        this.treeItem = treeItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return treeItem.getDisplayCaption();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof TreeEntryModelClass)) {
            return false;
        }
        return this.treeItem.equals(((TreeEntryModelClass) obj).getTreeItem());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.treeItem.hashCode();
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public TreeItem getTreeItem() {
        return treeItem;
    }

    public void setTreeItem(TreeItem treeItem) {
        this.treeItem = treeItem;
    }
    
}
