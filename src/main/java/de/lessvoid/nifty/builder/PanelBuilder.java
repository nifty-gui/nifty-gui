package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class PanelBuilder extends ElementBuilder {
  private PanelCreator creator = new PanelCreator();

  public PanelBuilder() {
    initialize(creator);
  }

  public PanelBuilder(final String id) {
    this();
    this.id(id);
  }

  @Override
  protected Element buildInternal(final Nifty nifty, final Screen screen, final Element parent) {
    return creator.create(nifty, screen, parent);
  }
}
