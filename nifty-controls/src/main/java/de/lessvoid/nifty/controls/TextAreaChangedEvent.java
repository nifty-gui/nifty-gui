package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

/**
 * Nifty generates this event when the current text of an TextField changes.
 *
 * @author void
 */
public class TextAreaChangedEvent implements NiftyEvent {
  @Nonnull
  private final TextArea textField;
  @Nonnull
  private final String currentText;

  public TextAreaChangedEvent(@Nonnull final TextArea textFieldControl, @Nonnull final String currentText) {
    this.textField = textFieldControl;
    this.currentText = currentText;
  }

  @Nonnull
  public TextArea getTextFieldControl() {
    return textField;
  }

  @Nonnull
  public String getText() {
    return currentText;
  }
}
