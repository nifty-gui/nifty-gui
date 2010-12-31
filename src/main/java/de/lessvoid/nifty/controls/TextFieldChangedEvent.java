package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the current text of an TextField changes.
 * @author void
 */
public class TextFieldChangedEvent implements NiftyEvent<Void> {
  private String currentText;

  public TextFieldChangedEvent(final String currentText) {
    this.currentText = currentText;
  }

  public String getSelection() {
    return currentText;
  }
}
