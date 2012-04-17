/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.controls.tabs.TabGroupControl;

/**
 * @author ractoc
 */
public class TabSelectedEvent implements NiftyEvent<Void> {
  private TabGroupControl tabsControl;
  private String selectedTabId;

  public TabSelectedEvent(TabGroupControl tabsControl, String selectedTabId) {
    this.tabsControl = tabsControl;
    this.selectedTabId = selectedTabId;
  }

  public String getSelectedTabId() {
    return selectedTabId;
  }

  public void setSelectedTabId(String selectedTabId) {
    this.selectedTabId = selectedTabId;
  }

  public TabGroupControl getTabsControl() {
    return tabsControl;
  }

  public void setTabsControl(TabGroupControl tabsControl) {
    this.tabsControl = tabsControl;
  }


}
