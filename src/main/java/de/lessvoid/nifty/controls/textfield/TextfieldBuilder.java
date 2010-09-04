package de.lessvoid.nifty.controls.textfield;

import de.lessvoid.nifty.builder.ControlBuilder;

public class TextfieldBuilder extends ControlBuilder {
  public TextfieldBuilder(final String name) {
    super("textfield");
  }

  public TextfieldBuilder(final String name, final String id) {
    super(id, "textfield");
  }
}
