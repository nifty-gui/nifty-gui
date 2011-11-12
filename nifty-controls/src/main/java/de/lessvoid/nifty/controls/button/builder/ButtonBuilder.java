package de.lessvoid.nifty.controls.button.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class ButtonBuilder extends ControlBuilder {
  public ButtonBuilder(final String id) {
    super(id, "button");
  }

  public ButtonBuilder(final String id, final String buttonLabel) {
    super(id, "button");
    label(buttonLabel);
  }

  public void label(final String label) {
    set("label", label);
  }
}
