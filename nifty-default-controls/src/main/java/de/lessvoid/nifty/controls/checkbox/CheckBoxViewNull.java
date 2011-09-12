package de.lessvoid.nifty.controls.checkbox;

import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;

/**
 * A Null implementation of CheckBoxView that does nothing.
 * @author void
 */
public class CheckBoxViewNull implements CheckBoxView {

  @Override
  public void update(final boolean checked) {
  }

  @Override
  public void publish(final CheckBoxStateChangedEvent event) {
  }
}
