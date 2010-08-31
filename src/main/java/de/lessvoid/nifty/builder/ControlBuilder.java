package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class ControlBuilder extends ElementBuilder {
  
  private CustomControlCreator creator;
  
  public ControlBuilder(String name) {
    creator = new CustomControlCreator(name);
    initialize(creator);
  }
  
  public ControlBuilder(String id, String name) {
    creator = new CustomControlCreator(id, name);
    initialize(creator);
  }
  
  @Override
  protected Element buildInternal(Nifty nifty, Screen screen, Element parent) {
    return creator.create(nifty, screen, parent);
  }
  
}
