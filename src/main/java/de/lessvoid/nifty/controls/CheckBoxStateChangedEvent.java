package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the state of the CheckBox changes.
 * @author void
 */
@SuppressWarnings("rawtypes")
public class CheckBoxStateChangedEvent implements NiftyEvent {
  private boolean checked;

  public CheckBoxStateChangedEvent(final boolean checkedState) {
    this.checked = checkedState;
  }

  public boolean isChecked() {
    return checked;
  }
}
