/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.elements.Element;

/**
 *
 * @author ractoc
 */
public interface Tabs extends NiftyControl {
    
    /**
     * Adds a single tab to the end of the Tab list.
     * @param tab Tab to be added.
     */
    public void addTab(Element tab);
    
    /**
     * Removes the tab at the specified index.
     * @param index Index of the tab to remove.
     */
    public void removeTab(int index);
    
    /**
     * Sets the active tab to the specified index.
     * @param index The index of the tab to make the active tab.
     */
    public void setActiveTab(int index);
    
    /**
     * Sets the active tab based on the tabId;
     * @param tabId The TabId of the tab to make the active tab.
     */
    public void setActiveTab(String tabId);
    
    
}
