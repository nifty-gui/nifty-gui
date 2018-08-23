package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;

import javax.annotation.Nonnull;

public class ControlBuilder extends ElementBuilder {
  @Nonnull
  private final CustomControlCreator creator;

  private ControlBuilder(@Nonnull final CustomControlCreator creator) {
    super(creator);
    this.creator = creator;
  }

  public ControlBuilder(@Nonnull final String name) {
    this(new CustomControlCreator(name));
  }

  public ControlBuilder(@Nonnull final String id, @Nonnull final String name) {
    this(new CustomControlCreator(id, name));
  }

  public ControlBuilder parameter(@Nonnull final String name, @Nonnull final String value) {
    creator.parameter(name, value);
    return this;
  }
}
