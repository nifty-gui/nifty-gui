package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the state of the CheckBox changes.
 * @author void
 */
public class CheckBoxStateChangedEvent implements NiftyEvent {
  private CheckBox checkbox;
  private boolean checked;

  public CheckBoxStateChangedEvent(final CheckBox checkbox, final boolean checkedState) {
    this.checkbox = checkbox;
    this.checked = checkedState;
  }

  public CheckBox getCheckBox() {
    return checkbox;
  }

  public boolean isChecked() {
    return checked;
  }
}
