package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.ImageCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class ImageBuilder extends ElementBuilder {
  private ImageCreator creator = new ImageCreator();

  public ImageBuilder() {
    initialize(creator);
  }

  public ImageBuilder(final String id) {
    this();
    this.id(id);
  }

  @Override
  protected Element buildInternal(final Nifty nifty, final Screen screen, final Element parent) {
    return creator.create(nifty, screen, parent);
  }
}
