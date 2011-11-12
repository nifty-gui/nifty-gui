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
  public Element build(final Nifty nifty, final Screen screen, final Element parent) {
    Element e = super.build(nifty, screen, parent);
    screen.addLayerElement(e);
    screen.processAddAndRemoveLayerElements();
    return e;
  }
}
