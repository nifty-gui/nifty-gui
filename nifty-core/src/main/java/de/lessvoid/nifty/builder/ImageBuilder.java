package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.ImageCreator;

public class ImageBuilder extends ElementBuilder {
  private ImageCreator creator = new ImageCreator();
  
  public ImageBuilder() {
    initialize(creator);
  }
  
  public ImageBuilder(final String id) {
    this();
    this.id(id);
  }
}
