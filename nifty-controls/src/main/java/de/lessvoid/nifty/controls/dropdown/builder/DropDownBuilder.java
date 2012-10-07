package de.lessvoid.nifty.controls.dropdown.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class DropDownBuilder extends ControlBuilder {
  public DropDownBuilder(final String id) {
    super(id, "dropDown");
  }

  public void viewConverterClass(final Class<?> clazz) {
    set("viewConverterClass", clazz.getName());
  }
}
