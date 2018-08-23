package de.lessvoid.nifty.controls.radiobutton.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class RadioButtonBuilder extends ControlBuilder {
  public RadioButtonBuilder() {
    super("radioButton");
  }

  public RadioButtonBuilder(@Nonnull final String id) {
    super(id, "radioButton");
  }

  public RadioButtonBuilder group(@Nonnull final String group) {
    set("group", group);
    return this;
  }
}
