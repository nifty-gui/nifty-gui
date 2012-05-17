package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when a single RadioButton changes its state. It
 * gets selected or unselected. If you're more interested in the state of a whole
 * RadioGroup (a linked collection of individual RadioButtons with only a single
 * one being allowed to be active) you might want to look at the
 * RadioGroupSelectionChangedEvent. 
 * @author void
 */
public class RadioButtonStateChangedEvent implements NiftyEvent {
  private RadioButton radioButton;
  private boolean selected;

  public RadioButtonStateChangedEvent(final RadioButton radioButton, final boolean newSelected) {
    this.radioButton = radioButton;
    this.selected = newSelected;
  }

  public RadioButton getRadioButton() {
    return radioButton;
  }

  public boolean isSelected() {
    return selected;
  }
}
