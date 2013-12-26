package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

/**
 * Nifty generates this event when a Slider value has been changed.
 *
 * @author void
 */
public class SliderChangedEvent implements NiftyEvent {
  @Nonnull
  private final Slider slider;
  private final float value;

  public SliderChangedEvent(@Nonnull final Slider slider, final float newValue) {
    this.slider = slider;
    this.value = newValue;
  }

  @Nonnull
  public Slider getSlider() {
    return slider;
  }

  public float getValue() {
    return value;
  }
}
