/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

/**
 * @author ractoc
 */
public interface TreeBox<T> extends NiftyControl {
    
    void setTree(TreeItem<T> treeRoot);
}
