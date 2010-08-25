package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.LayerCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class LayerBuilder extends ElementBuilder {
  private LayerCreator creator = new LayerCreator();

  public LayerBuilder() {
    initialize(creator);
  }

  public LayerBuilder(final String id) {
    this();
    this.id(id);
  }

  @Override
  protected Element buildInternal(final Nifty nifty, final Screen screen, final Element parent) {
    return creator.create(nifty, screen, parent);
  }
}
