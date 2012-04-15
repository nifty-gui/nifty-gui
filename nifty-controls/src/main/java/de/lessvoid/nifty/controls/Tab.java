/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

/**
 * This interface allows to take control over a single tab. This tab is supposed to be the part of a {@link TabGroup}.
 * For the most aspects it behaves like a simple panel.
 *
 * @author ractoc
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface Tab extends NiftyControl {
  /**
   * Get the caption that is currently set for this tab.
   *
   * @return the current caption of this tab
   */
  String getCaption();

  /**
   * Get the parent group of tabs this tab is a member of.
   *
   * @return the tab group this tab belongs to or {@code null} in case this tab is not yet assigned to a tab group
   */
  TabGroup getParentGroup();

  /**
   * Check if this tab was added to a parent tab group.
   *
   * @return {@code true} if this tab is part of a group
   */
  boolean hasParent();

  /**
   * Check if this tab is the one that is currently visible in a tab group.
   *
   * @return {@code true} if this tab is the one currently displayed
   */
  boolean isVisible();

  /**
   * Set the caption of the tab. This is the text that will be displayed in the button that turns this tab visible.
   *
   * @param caption the text to display
   */
  void setCaption(String caption);
}
