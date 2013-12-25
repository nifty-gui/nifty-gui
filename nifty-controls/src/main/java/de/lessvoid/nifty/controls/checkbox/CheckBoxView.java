package de.lessvoid.nifty.controls.checkbox;

import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;

/**
 * The representation of a CheckBoxView from the world of a CheckBox.
 *
 * @author void
 */

public interface CheckBoxView {
  /**
   * Update the View with the new state.
   *
   * @param checked new state of the checkbox
   */
  void update(final boolean checked);

  /**
   * Publish this event.
   *
   * @param event the event to publish
   */
  void publish(CheckBoxStateChangedEvent event);
}
