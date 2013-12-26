package de.lessvoid.nifty.controls.textfield.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class TextFieldBuilder extends ControlBuilder {
  public TextFieldBuilder() {
    super("textfield");
  }

  public TextFieldBuilder(@Nonnull final String id) {
    super(id, "textfield");
  }

  public TextFieldBuilder(@Nonnull final String id, @Nonnull final String initialText) {
    super(id, "textfield");
    text(initialText);
  }

  public void passwordChar(final Character passwordChar) {
    set("passwordChar", String.valueOf(passwordChar));
  }

  public void maxLength(final int maxLength) {
    set("maxLength", Integer.toString(maxLength));
  }

  public void filter(@Nonnull final CharSequence filter) {
    set("filter", filter.toString());
  }
}
