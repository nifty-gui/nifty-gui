package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.PanelCreator;

public class PanelBuilder extends ElementBuilder {
  private PanelCreator creator = new PanelCreator();

  public PanelBuilder() {
    initialize(creator);
  }

  public PanelBuilder(final String id) {
    this();
    this.id(id);
  }
}
