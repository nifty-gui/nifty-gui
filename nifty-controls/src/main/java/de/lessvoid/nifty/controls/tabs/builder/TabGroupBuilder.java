/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.tabs.builder;

import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;

import javax.annotation.Nonnull;

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
  public TabGroupBuilder(@Nonnull final String id) {
    super(id, "tabGroup"); //NON-NLS
  }

  /**
   * The protected constructor that allows setting the element id and the control type name of the created control. This
   * constructor should be used in case this control is extended by a new one.
   *
   * @param id       the id of the element to be created
   * @param typeName the name of the control type
   */
  protected TabGroupBuilder(@Nonnull final String id, @Nonnull final String typeName) {
    super(id, typeName);
  }

  /**
   * @throws UnsupportedOperationException in case the control is no {@link TabBuilder}
   */
  @Override
  public TabGroupBuilder control(@Nonnull final ControlBuilder controlBuilder) {
    if (controlBuilder instanceof TabBuilder) {
      super.control(controlBuilder);
      return this;
    } else {
      throw new UnsupportedOperationException("Tab groups do not accept any controls other then tabs as children");
    }
  }

  /**
   * @throws UnsupportedOperationException always
   */
  @Override
  @SuppressWarnings("RefusedBequest")
  public TabGroupBuilder image(@Nonnull final ImageBuilder imageBuilder) {
    throw new UnsupportedOperationException("Tab groups do not accept any elements other then tabs as children");
  }

  /**
   * @throws UnsupportedOperationException always
   */
  @Override
  @SuppressWarnings("RefusedBequest")
  public TabGroupBuilder panel(@Nonnull final PanelBuilder panelBuilder) {
    throw new UnsupportedOperationException("Tab groups do not accept any elements other then tabs as children");
  }

  /**
   * Add a tab to this tab group.
   *
   * @param tabBuilder the builder of the new tab
   */
  public TabGroupBuilder tab(@Nonnull final TabBuilder tabBuilder) {
    super.control(tabBuilder);
    return this;
  }

  /**
   * @throws UnsupportedOperationException always
   */
  @Override
  @SuppressWarnings("RefusedBequest")
  public TabGroupBuilder text(@Nonnull final TextBuilder textBuilder) {
    throw new UnsupportedOperationException("Tab groups do not accept any elements other then tabs as children");
  }
}
