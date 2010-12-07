package de.lessvoid.nifty.controls.listbox.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class ListBoxBuilder extends ControlBuilder {
  public ListBoxBuilder(final String name) {
    super("listBox");
  }

  public ListBoxBuilder(final String name, final String id) {
    super(id, "listBox");
  }
}
