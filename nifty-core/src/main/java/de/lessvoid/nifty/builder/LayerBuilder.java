package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.LayerCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;

public class LayerBuilder extends ElementBuilder {

  public LayerBuilder() {
    super(new LayerCreator());
  }

  public LayerBuilder(@Nonnull final String id) {
    this();
    this.id(id);
  }

  @Override
  public Element build(@Nonnull final Nifty nifty, @Nonnull final Screen screen, @Nonnull final Element parent) {
    Element e = super.build(nifty, screen, parent);
    screen.addLayerElement(e);
    screen.processAddAndRemoveLayerElements();
    return e;
  }
}
