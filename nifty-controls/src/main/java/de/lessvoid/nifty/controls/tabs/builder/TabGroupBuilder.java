/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.tabs.builder;

import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;

/**
 * This builder is used to create a tab group that is the parent to multiple tabs.
 *
 * @author ractoc
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class TabGroupBuilder extends ControlBuilder {
  /**
   * The default constructor to this class. Using this one will cause the tab group to get a random element id.
   */
  public TabGroupBuilder() {
    super("tabGroup"); //NON-NLS
  }

  /**
   * The constructor that allows setting the element id of this tab group.
   *
   * @param id the element id of this tab group
   */
  public TabGroupBuilder(final String id) {
    super(id, "tabGroup"); //NON-NLS
  }

  /**
   * The protected constructor that allows setting the element id and the control type name of the created control. This
   * constructor should be used in case this control is extended by a new one.
   *
   * @param id the id of the element to be created
   * @param typeName the name of the control type
   */
  protected TabGroupBuilder(final String id, final String typeName) {
    super(id, typeName);
  }

  /**
   * @throws UnsupportedOperationException in case the control is no {@link TabBuilder}
   */
  @Override
  public void control(final ControlBuilder controlBuilder) {
    if (controlBuilder instanceof TabBuilder) {
      super.control(controlBuilder);
    } else {
      throw new UnsupportedOperationException("Tab groups do not accept any controls other then tabs as children");
    }
  }

  /**
   * @throws UnsupportedOperationException always
   */
  @Override
  @SuppressWarnings("RefusedBequest")
  public void image(final ImageBuilder imageBuilder) {
    throw new UnsupportedOperationException("Tab groups do not accept any elements other then tabs as children");
  }

  /**
   * @throws UnsupportedOperationException always
   */
  @Override
  @SuppressWarnings("RefusedBequest")
  public void panel(final PanelBuilder panelBuilder) {
    throw new UnsupportedOperationException("Tab groups do not accept any elements other then tabs as children");
  }

  /**
   * Add a tab to this tab group.
   *
   * @param tabBuilder the builder of the new tab
   */
  public void tab(final TabBuilder tabBuilder) {
    super.control(tabBuilder);
  }

  /**
   * @throws UnsupportedOperationException always
   */
  @Override
  @SuppressWarnings("RefusedBequest")
  public void text(final TextBuilder textBuilder) {
    throw new UnsupportedOperationException("Tab groups do not accept any elements other then tabs as children");
  }
}
