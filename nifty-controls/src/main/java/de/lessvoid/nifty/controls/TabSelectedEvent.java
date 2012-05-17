/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * This event is fired once a tab is selected by the user and becomes visible.
 *
 * @author ractoc
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TabSelectedEvent implements NiftyEvent {
  /**
   * The group that is the parent of the tab that got selected.
   */
  private final TabGroup group;

  /**
   * The tab that was selected.
   */
  private final Tab tab;

  /**
   * The index of the selected tab.
   */
  private final int index;

  /**
   * + Create a instance of this event and set all the values that are transferred in such a event.
   *
   * @param tabGroup the tab group control that is the parent of the effected tab
   * @param tabControl the tab that is now visible
   * @param tabIndex the index of the tab that is now visible
   */
  public TabSelectedEvent(final TabGroup tabGroup, final Tab tabControl, final int tabIndex) {
    group = tabGroup;
    tab = tabControl;
    index = tabIndex;
  }

  /**
   * Get the tab group that is the parent of the tab that became visible.
   *
   * @return the tab group
   */
  private TabGroup getParentGroup() {
    return group;
  }

  /**
   * Get the tab that became visible.
   *
   * @return the tab that is now visible
   */
  private Tab getTab() {
    return tab;
  }

  /**
   * Get the index of the tab that was set visible.
   *
   * @return the index of the tab that is now visible
   */
  private int getIndex() {
    return index;
  }
}
