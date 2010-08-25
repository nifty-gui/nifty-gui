package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.TextCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class TextBuilder extends ElementBuilder {
  private TextCreator creator = new TextCreator("");

  public TextBuilder() {
    initialize(creator);
  }

  public TextBuilder(final String id) {
    this();
    this.id(id);
  }

  public void text(final String text) {
    creator.setText(text);
  }

  @Override
  protected Element buildInternal(final Nifty nifty, final Screen screen, final Element parent) {
    return creator.create(nifty, screen, parent);
  }
}
