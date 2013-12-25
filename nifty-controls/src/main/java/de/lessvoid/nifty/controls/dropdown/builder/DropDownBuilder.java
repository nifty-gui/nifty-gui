package de.lessvoid.nifty.controls.dropdown.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class DropDownBuilder extends ControlBuilder {
  public DropDownBuilder() {
    super("dropDown");
  }

  public DropDownBuilder(@Nonnull final String id) {
    super(id, "dropDown");
  }

  public void viewConverterClass(@Nonnull final Class<?> clazz) {
    set("viewConverterClass", clazz.getName());
  }

  public void displayItems(final int displayItems) {
    set("displayItems", String.valueOf(displayItems));
  }
}
