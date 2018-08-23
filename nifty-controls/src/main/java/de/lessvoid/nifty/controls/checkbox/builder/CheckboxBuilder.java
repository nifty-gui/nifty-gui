package de.lessvoid.nifty.controls.checkbox.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class CheckboxBuilder extends ControlBuilder {
  public CheckboxBuilder() {
    super("checkbox");
  }

  public CheckboxBuilder(@Nonnull final String id) {
    super(id, "checkbox");
  }

  public CheckboxBuilder checked(final boolean checked) {
    set("checked", String.valueOf(checked));
    return this;
  }
}
