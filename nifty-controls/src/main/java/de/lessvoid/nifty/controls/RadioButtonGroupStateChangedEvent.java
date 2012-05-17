package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the RadioButton selection in a RadioGroup (which is a linked
 * collection of individual RadioButtons) has changed.
 * @author void
 */
public class RadioButtonGroupStateChangedEvent implements NiftyEvent {
  private RadioButton selectedRadioButton;
  private RadioButton previousSelectedRadioButton;

  public RadioButtonGroupStateChangedEvent(final RadioButton radioButton, final RadioButton previousSelectedRadioButton) {
    this.selectedRadioButton = radioButton;
    this.previousSelectedRadioButton = previousSelectedRadioButton;
  }

  public String getSelectedId() {
    if (selectedRadioButton == null) {
      return null;
    }
    return selectedRadioButton.getId();
  }

  public RadioButton getSelectedRadioButton() {
    return selectedRadioButton;
  }

  public RadioButton getPreviousSelectedRadioButton() {
    return previousSelectedRadioButton;
  }

  public String getPreviousSelectedId() {
    if (previousSelectedRadioButton == null) {
      return null;
    }
    return previousSelectedRadioButton.getId();
  }
}
