package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.TextCreator;

public class TextBuilder extends ElementBuilder {
  private TextCreator creator = new TextCreator("");

  public TextBuilder() {
    initialize(creator);
  }

  public TextBuilder(final String id) {
    this();
    this.id(id);
  }
}
