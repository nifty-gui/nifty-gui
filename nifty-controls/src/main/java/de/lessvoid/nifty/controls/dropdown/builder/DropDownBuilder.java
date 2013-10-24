package de.lessvoid.nifty.controls.dropdown.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class DropDownBuilder extends ControlBuilder {
  public DropDownBuilder(final String id) {
    super(id, "dropDown");
  }

  public void viewConverterClass(final Class<?> clazz) {
    set("viewConverterClass", clazz.getName());
  }

  public void displayItems(final int displayItems) {
    set("displayItems", String.valueOf(displayItems));
  }
}
