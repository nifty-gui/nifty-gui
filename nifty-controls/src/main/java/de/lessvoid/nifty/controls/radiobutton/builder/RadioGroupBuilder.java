package de.lessvoid.nifty.controls.radiobutton.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class RadioGroupBuilder extends ControlBuilder {
  public RadioGroupBuilder() {
    super("radioButtonGroup");
  }

  public RadioGroupBuilder(@Nonnull final String id) {
    super(id, "radioButtonGroup");
  }
}
