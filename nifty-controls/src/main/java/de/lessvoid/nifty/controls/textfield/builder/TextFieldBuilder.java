package de.lessvoid.nifty.controls.textfield.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class TextFieldBuilder extends ControlBuilder {
  public TextFieldBuilder() {
    super("textfield");
  }

  public TextFieldBuilder(final String id) {
    super(id, "textfield");
  }

  public TextFieldBuilder(final String id, final String initialText) {
    super(id, "textfield");
    text(initialText);
  }

  public void passwordChar(final Character passwordChar) {
    set("passwordChar", String.valueOf(passwordChar));
  }

  public void maxLength(final int maxLength) {
    set("maxLength", Integer.toString(maxLength));
  }

  public void filter(final CharSequence filter) {
    set("filter", filter.toString());
  }
}
