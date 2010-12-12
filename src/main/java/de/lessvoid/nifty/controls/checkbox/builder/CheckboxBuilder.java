package de.lessvoid.nifty.controls.checkbox.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class CheckboxBuilder extends ControlBuilder {
  public CheckboxBuilder(final String name) {
    super("checkbox");
  }

  public CheckboxBuilder(final String name, final String id) {
    super(id, "checkbox");
  }
}
