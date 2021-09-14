package de.lessvoid.nifty.controls.textarea.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class TextAreaBuilder extends ControlBuilder {
  public TextAreaBuilder() {
    super("textarea");
  }

  public TextAreaBuilder(@Nonnull final String id) {
    super(id, "textarea");
  }

  public TextAreaBuilder(@Nonnull final String id, @Nonnull final String initialText) {
    super(id, "textarea");
    text(initialText);
  }


  public void maxLength(final int maxLength) {
    set("maxLength", Integer.toString(maxLength));
  }

  public void filter(@Nonnull final CharSequence filter) {
    set("filter", filter.toString());
  }
}
