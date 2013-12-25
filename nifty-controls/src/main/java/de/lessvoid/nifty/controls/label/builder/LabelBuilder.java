package de.lessvoid.nifty.controls.label.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class LabelBuilder extends ControlBuilder {
  public LabelBuilder() {
    super("label");
  }

  public LabelBuilder(@Nonnull final String id) {
    super(id, "label");
  }

  public LabelBuilder(@Nonnull final String id, @Nonnull final String text) {
    super(id, "label");
    text(text);
  }

  public void label(@Nonnull final String label) {
    text(label);
  }

  public void wrap(final boolean enabled) {
    set("wrap", Boolean.toString(enabled));
  }
}
