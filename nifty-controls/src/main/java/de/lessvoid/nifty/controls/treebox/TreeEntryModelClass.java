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
public class TreeEntryModelClass<T> {
   
    private TreeItem<T> treeItem;
    
    private int indent;
    
    private boolean activeItem;
    
    public TreeEntryModelClass(int indent, TreeItem<T> treeItem) {
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
        return this.treeItem.equals(((TreeEntryModelClass<?>) obj).getTreeItem());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.treeItem.hashCode();
    }

    public boolean isActiveItem() {
        return activeItem;
    }

    public void setActiveItem(boolean activeItem) {
        this.activeItem = activeItem;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public TreeItem<T> getTreeItem() {
        return treeItem;
    }

    public void setTreeItem(TreeItem<T> treeItem) {
        this.treeItem = treeItem;
    }
    
}
