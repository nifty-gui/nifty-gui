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
     * Sets the selected tab to the specified index.
     * @param index The index of the tab to make the selected tab.
     */
    public void setSelectedTab(int index);
    
    /**
     * Sets the selected tab based on the tabId;
     * @param tabId The TabId of the tab to make the selected tab.
     */
    public void setSelectedTab(String tabId);
    
    /**
     * Gets the selected tab to the specified index.
     * @return The id of the selected tab.
     */
    public String getSelectedTab();
    
    /**
     * Gets the selected tab to the specified index.
     * @return The index of the selected tab.
     */
    public int getSelectedTabIndex();
    
    
}
