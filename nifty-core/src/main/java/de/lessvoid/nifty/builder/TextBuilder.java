package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.TextCreator;

import javax.annotation.Nonnull;

public class TextBuilder extends ElementBuilder {
  @Nonnull
  private final TextCreator creator;

  private TextBuilder(@Nonnull final TextCreator creator) {
    super(creator);
    this.creator = creator;
  }


  public TextBuilder() {
    this(new TextCreator(""));
  }

  public TextBuilder(@Nonnull final String id) {
    this();
    this.id(id);
  }

  public TextBuilder wrap(final boolean wrap) {
    creator.setWrap(wrap);
    return this;
  }
}
