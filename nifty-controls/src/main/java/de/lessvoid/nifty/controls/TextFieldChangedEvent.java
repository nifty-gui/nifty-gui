package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

/**
 * Nifty generates this event when the current text of an TextField changes.
 *
 * @author void
 */
public class TextFieldChangedEvent implements NiftyEvent {
  @Nonnull
  private final TextField textField;
  @Nonnull
  private final String currentText;

  public TextFieldChangedEvent(@Nonnull final TextField textFieldControl, @Nonnull final String currentText) {
    this.textField = textFieldControl;
    this.currentText = currentText;
  }

  @Nonnull
  public TextField getTextFieldControl() {
    return textField;
  }

  @Nonnull
  public String getText() {
    return currentText;
  }
}
