package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.PopupCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class PopupBuilder extends ElementBuilder {
  private PopupCreator creator = new PopupCreator();
  
  public PopupBuilder() {
    initialize(creator);
  }
  
  public PopupBuilder(final String id) {
    this();
    this.id(id);
  }
  
  @Override
  protected Element buildInternal(final Nifty nifty, final Screen screen, final Element parent) {
    return creator.create(nifty, screen);
  }
}
