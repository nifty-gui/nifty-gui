package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

/**
 * Nifty generates this event when the state of the CheckBox changes.
 *
 * @author void
 */
public class CheckBoxStateChangedEvent implements NiftyEvent {
  @Nonnull
  private final CheckBox checkbox;
  private final boolean checked;

  public CheckBoxStateChangedEvent(@Nonnull final CheckBox checkbox, final boolean checkedState) {
    this.checkbox = checkbox;
    this.checked = checkedState;
  }

  @Nonnull
  public CheckBox getCheckBox() {
    return checkbox;
  }

  public boolean isChecked() {
    return checked;
  }
}
