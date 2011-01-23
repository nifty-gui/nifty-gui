package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when a Slider value has been changed.
 * @author void
 */
public class SliderChangedEvent implements NiftyEvent<Void> {
  private float value;

  public SliderChangedEvent(final float newValue) {
    this.value = newValue;
  }

  public float getValue() {
    return value;
  }
}
