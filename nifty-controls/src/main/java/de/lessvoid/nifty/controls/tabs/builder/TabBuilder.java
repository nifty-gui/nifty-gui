/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.tabs.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

/**
 * This is the builder used to create the tabs that can be displayed inside a tab group.
 *
 * @author ractoc
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TabBuilder extends ControlBuilder {
  /**
   * Create the tab with a specified title and a randomly chosen element id.
   *
   * @param caption the caption that is displayed for this tab
   */
  public TabBuilder(final String caption) {
    super("tab"); //NON-NLS
    caption(caption);
  }

  /**
   * Create a tab with a specified title and the specified element id.
   *
   * @param id the element id of the new tab
   * @param caption the caption of the new tab
   */
  public TabBuilder(final String id, final String caption) {
    super(id, "tab");
    caption(caption);
  }

  /**
   * The protected constructor for overwriting.
   *
   * @param id the element Id of the new tab
   * @param typeName the control type name that is supposed to be used when creating the tab
   * @param caption the caption that is supposed to be displayed for this tab
   */
  protected TabBuilder(final String id, final String typeName, final String caption) {
    super(id, typeName);
    caption(caption);
  }

  /**
   * Set the caption that is displayed on the button that will show this button.
   *
   * @param caption the caption on the button that will be assigned to this tab
   */
  public void caption(final String caption) {
    set("caption", caption);
  }
}
