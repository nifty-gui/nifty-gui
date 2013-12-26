package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nullable;

/**
 * Nifty generates this event when the RadioButton selection in a RadioGroup (which is a linked
 * collection of individual RadioButtons) has changed.
 *
 * @author void
 */
public class RadioButtonGroupStateChangedEvent implements NiftyEvent {
  @Nullable
  private final RadioButton selectedRadioButton;
  @Nullable
  private final RadioButton previousSelectedRadioButton;

  public RadioButtonGroupStateChangedEvent(
      @Nullable final RadioButton radioButton,
      @Nullable final RadioButton previousSelectedRadioButton) {
    this.selectedRadioButton = radioButton;
    this.previousSelectedRadioButton = previousSelectedRadioButton;
  }

  @Nullable
  public String getSelectedId() {
    if (selectedRadioButton == null) {
      return null;
    }
    return selectedRadioButton.getId();
  }

  @Nullable
  public RadioButton getSelectedRadioButton() {
    return selectedRadioButton;
  }

  @Nullable
  public RadioButton getPreviousSelectedRadioButton() {
    return previousSelectedRadioButton;
  }

  @Nullable
  public String getPreviousSelectedId() {
    if (previousSelectedRadioButton == null) {
      return null;
    }
    return previousSelectedRadioButton.getId();
  }
}
