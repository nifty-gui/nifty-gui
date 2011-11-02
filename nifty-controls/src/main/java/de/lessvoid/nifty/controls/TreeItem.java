/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.render.NiftyImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ractoc
 */
public class TreeItem<T> {

    private List<TreeItem<T>> treeNodes = new ArrayList<TreeItem<T>>();
    private TreeItem<T> parentItem;
    private boolean expanded;
    private T value;
    private String displayCaption;
    private NiftyImage displayIconCollapsed;
    private NiftyImage displayIconExpanded;

    public TreeItem(TreeItem<T> parentItem, T value, String displayCaption, NiftyImage displayIconCollapsed, NiftyImage displayIconExpanded, boolean expanded) {
        this.parentItem = parentItem;
        this.expanded = expanded;
        this.value = value;
        this.displayCaption = displayCaption;
        this.displayIconCollapsed = displayIconCollapsed;
        this.displayIconExpanded = displayIconExpanded;
    }

    public TreeItem(TreeItem<T> parentItem, T value, String displayCaption, NiftyImage displayIconCollapsed, NiftyImage displayIconExpanded) {
        this(parentItem, value, displayCaption, displayIconCollapsed, displayIconExpanded, false);
    }

    public TreeItem(TreeItem<T> parentItem, T value, String displayCaption, NiftyImage displayIcon, boolean expanded) {
        this(parentItem, value, displayCaption, displayIcon, null, expanded);
    }

    public TreeItem(TreeItem<T> parentItem, T value, String displayCaption, NiftyImage displayIcon) {
        this(parentItem, value, displayCaption, displayIcon, null, false);
    }
    
    public TreeItem() {
        this(null, null, "Root", null, null, true);
    }

    public void addTreeItem(TreeItem<T> item) {
        treeNodes.add(item);
    }

    public void addTreeItems(List<TreeItem<T>> items) {
        treeNodes.addAll(items);
    }

    public List<TreeItem<T>> getTreeItems() {
        return treeNodes;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public TreeItem<T> getParentItem() {
        return parentItem;
    }

    public void setParentItem(TreeItem<T> parentItem) {
        this.parentItem = parentItem;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getDisplayCaption() {
        return displayCaption;
    }

    public void setDisplayCaption(String displayCaption) {
        this.displayCaption = displayCaption;
    }

    public NiftyImage getDisplayIconCollapsed() {
        return displayIconCollapsed;
    }

    public void setDisplayIconCollapsed(NiftyImage displayIconCollapsed) {
        this.displayIconCollapsed = displayIconCollapsed;
    }

    public NiftyImage getDisplayIconExpanded() {
        return displayIconExpanded;
    }

    public void setDisplayIconExpanded(NiftyImage displayIconExpanded) {
        this.displayIconExpanded = displayIconExpanded;
    }

    public boolean findAncestor(TreeItem<T> ancestor) {
        if (this.equals(ancestor)) {
            return true;
        } else if(this.displayCaption.equals("Root")) {
            return false;
        }else {
            return parentItem.findAncestor(ancestor);
        }
    }
    
    
}
