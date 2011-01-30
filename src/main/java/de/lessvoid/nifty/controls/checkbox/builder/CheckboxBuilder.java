package de.lessvoid.nifty.controls.checkbox.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class CheckboxBuilder extends ControlBuilder {
  public CheckboxBuilder() {
    super("checkbox");
  }

  public CheckboxBuilder(final String id) {
    super(id, "checkbox");
  }

  public void checked(final boolean checked) {
    set("checked", String.valueOf(checked));
  }
}
