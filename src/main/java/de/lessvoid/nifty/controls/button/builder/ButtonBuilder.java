package de.lessvoid.nifty.controls.button.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class ButtonBuilder extends ControlBuilder {
  public ButtonBuilder(final String name) {
    super("button");
    label(name);
  }

  public ButtonBuilder(final String name, final String id) {
    super(id, "button");
    label(name);
  }

  public void label(final String label) {
    set("label", label);
  }
}
