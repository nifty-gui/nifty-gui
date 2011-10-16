/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.controls.tabs.TabsControl;

/**
 *
 * @author ractoc
 */
public class TabSelectedEvent implements NiftyEvent<Void> {
    private TabsControl tabsControl;
    private String selectedTabId;
    
    public TabSelectedEvent(TabsControl tabsControl, String selectedTabId) {
        this.tabsControl = tabsControl;
        this.selectedTabId = selectedTabId;
    }
}
