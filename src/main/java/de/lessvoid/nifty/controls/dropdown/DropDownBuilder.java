package de.lessvoid.nifty.controls.dropdown;

import de.lessvoid.nifty.builder.ControlBuilder;

public class DropDownBuilder extends ControlBuilder {
  public DropDownBuilder(final String name) {
    super("dropDownControl");
  }

  public DropDownBuilder(final String name, final String id) {
    super(id, "dropDownControl");
  }
}
