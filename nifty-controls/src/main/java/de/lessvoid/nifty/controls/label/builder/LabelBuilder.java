package de.lessvoid.nifty.controls.label.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class LabelBuilder extends ControlBuilder {
  public LabelBuilder() {
    super("label");
  }

  public LabelBuilder(final String id) {
    super(id, "label");
  }

  public LabelBuilder(final String id, final String text) {
    super(id, "label");
    text(text);
  }

  public void label(final String label) {
    text(label);
  }

  public void wrap(final boolean enabled) {
    set("wrap", Boolean.toString(enabled));
  }
}
