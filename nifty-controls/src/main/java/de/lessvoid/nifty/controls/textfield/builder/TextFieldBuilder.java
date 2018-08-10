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

  public TextFieldBuilder passwordChar(final Character passwordChar) {
    set("passwordChar", String.valueOf(passwordChar));
    return this;
  }

  public TextFieldBuilder maxLength(final int maxLength) {
    set("maxLength", Integer.toString(maxLength));
    return this;
  }

  public TextFieldBuilder filter(@Nonnull final CharSequence filter) {
    set("filter", filter.toString());
    return this;
  }
}
