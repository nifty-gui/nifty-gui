package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.LabelCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class LabelBuilder extends ElementBuilder {
  private LabelCreator creator = new LabelCreator("");

  public LabelBuilder() {
    initialize(creator);
  }

  public LabelBuilder(final String id) {
    this();
    this.id(id);
  }

  @Override
  protected Element buildInternal(final Nifty nifty, final Screen screen, final Element parent) {
    return creator.create(nifty, screen, parent);
  }
}
