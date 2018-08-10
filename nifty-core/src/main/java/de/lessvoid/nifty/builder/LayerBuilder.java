package de.lessvoid.nifty.builder;

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
  public Element build(@Nonnull final Element parent) {
    Element e = super.build(parent);
    Screen screen = parent.getScreen();
    screen.addLayerElement(e);
    screen.processAddAndRemoveLayerElements();
    return e;
  }

  @Override
  public Element build(@Nonnull final Element parent, final int index) {
    Element e = super.build(parent, index);
    Screen screen = parent.getScreen();
    screen.addLayerElement(e);
    screen.processAddAndRemoveLayerElements();
    return e;
  }
}
