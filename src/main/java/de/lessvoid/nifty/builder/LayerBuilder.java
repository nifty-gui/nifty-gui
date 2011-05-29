package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.LayerCreator;

public class LayerBuilder extends ElementBuilder {
  private LayerCreator creator = new LayerCreator();
  
  public LayerBuilder() {
    initialize(creator);
  }
  
  public LayerBuilder(final String id) {
    this();
    this.id(id);
  }
}
