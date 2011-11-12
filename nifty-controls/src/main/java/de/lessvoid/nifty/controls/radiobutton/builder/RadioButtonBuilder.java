package de.lessvoid.nifty.controls.radiobutton.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class RadioButtonBuilder extends ControlBuilder {
  public RadioButtonBuilder() {
    super("radioButton");
  }

  public RadioButtonBuilder(final String id) {
    super(id, "radioButton");
  }

  public void group(final String group) {
    set("group", group);
  }
}
