package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when a Slider value has been changed.
 * @author void
 */
public class SliderChangedEvent implements NiftyEvent {
  private Slider slider;
  private float value;

  public SliderChangedEvent(final Slider slider, final float newValue) {
    this.slider = slider;
    this.value = newValue;
  }

  public Slider getSlider() {
    return slider;
  }

  public float getValue() {
    return value;
  }
}
