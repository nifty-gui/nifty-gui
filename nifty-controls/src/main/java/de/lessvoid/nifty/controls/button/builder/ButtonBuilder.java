package de.lessvoid.nifty.controls.button.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class ButtonBuilder extends ControlBuilder {
  public ButtonBuilder(@Nonnull final String id) {
    super(id, "button");
  }

  public ButtonBuilder(@Nonnull final String id, @Nonnull final String buttonLabel) {
    super(id, "button");
    label(buttonLabel);
  }

  public ButtonBuilder label(@Nonnull final String label) {
    set("label", label);
    return this;
  }
}
