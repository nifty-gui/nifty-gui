package de.lessvoid.nifty.controls.tabs;

import de.lessvoid.nifty.controls.TabGroup;

/**
 * This interface can be implemented in tab controls in case those controls need to be informed once they are added to a
 * tab group. This interface is not meant to be used in a final application. Its only used to nifty-control
 * development.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface TabGroupMember {
  /**
   * Set the tab group that is the parent of the tab.
   *
   * @param tabGroup the parent tab group
   */
  void setParentTabGroup(TabGroup tabGroup);
}
