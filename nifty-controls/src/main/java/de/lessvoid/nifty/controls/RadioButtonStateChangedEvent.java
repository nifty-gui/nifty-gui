package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

/**
 * Nifty generates this event when a single RadioButton changes its state. It
 * gets selected or unselected. If you're more interested in the state of a whole
 * RadioGroup (a linked collection of individual RadioButtons with only a single
 * one being allowed to be active) you might want to look at the
 * RadioGroupSelectionChangedEvent.
 *
 * @author void
 */
public class RadioButtonStateChangedEvent implements NiftyEvent {
  @Nonnull
  private final RadioButton radioButton;
  private final boolean selected;

  public RadioButtonStateChangedEvent(@Nonnull final RadioButton radioButton, final boolean newSelected) {
    this.radioButton = radioButton;
    this.selected = newSelected;
  }

  @Nonnull
  public RadioButton getRadioButton() {
    return radioButton;
  }

  public boolean isSelected() {
    return selected;
  }
}
