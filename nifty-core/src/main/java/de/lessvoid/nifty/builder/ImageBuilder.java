package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.ImageCreator;

import javax.annotation.Nonnull;

public class ImageBuilder extends ElementBuilder {

  public ImageBuilder() {
    super(new ImageCreator());
  }

  public ImageBuilder(@Nonnull final String id) {
    this();
    this.id(id);
  }
}
