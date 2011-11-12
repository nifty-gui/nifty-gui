package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the current text of an TextField changes.
 * @author void
 */
public class TextFieldChangedEvent implements NiftyEvent<Void> {
  private TextField textField;
  private String currentText;

  public TextFieldChangedEvent(final TextField textFieldControl, final String currentText) {
    this.textField = textFieldControl;
    this.currentText = currentText;
  }

  public TextField getTextFieldControl() {
    return textField;
  }

  public String getText() {
    return currentText;
  }
}
