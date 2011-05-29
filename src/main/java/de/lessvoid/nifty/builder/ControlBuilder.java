package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;

public class ControlBuilder extends ElementBuilder {
  private CustomControlCreator creator;
  
  public ControlBuilder(final String name) {
    creator = new CustomControlCreator(name);
    initialize(creator);
  }
  
  public ControlBuilder(final String id, final String name) {
    creator = new CustomControlCreator(id, name);
    initialize(creator);
  }

  public void parameter(final String name, final String value) {
    creator.parameter(name, value);
  }
}
