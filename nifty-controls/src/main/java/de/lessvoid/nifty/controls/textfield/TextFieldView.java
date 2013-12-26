package de.lessvoid.nifty.controls.textfield;

import javax.annotation.Nonnull;

public interface TextFieldView {
  /**
   * Publish a event indicating that the text in the text field got changed.
   *
   * @param newText the new text
   */
  void textChangeEvent(@Nonnull String newText);
}
