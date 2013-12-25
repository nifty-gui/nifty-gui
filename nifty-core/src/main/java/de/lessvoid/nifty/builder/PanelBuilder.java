package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.PanelCreator;

import javax.annotation.Nonnull;

public class PanelBuilder extends ElementBuilder {

  public PanelBuilder() {
    super(new PanelCreator());
  }

  public PanelBuilder(@Nonnull final String id) {
    this();
    this.id(id);
  }
}
