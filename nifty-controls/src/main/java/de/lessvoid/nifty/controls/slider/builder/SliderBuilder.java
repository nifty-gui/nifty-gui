package de.lessvoid.nifty.controls.slider.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class SliderBuilder extends ControlBuilder {
  public SliderBuilder(final boolean vertical) {
    super(getName(vertical));
  }

  public SliderBuilder(@Nonnull final String id, final boolean vertical) {
    super(id, getName(vertical));
  }

  public SliderBuilder min(final float min) {
    set("min", String.valueOf(min));
    return this;
  }

  public SliderBuilder max(final float max) {
    set("max", String.valueOf(max));
    return this;
  }

  public SliderBuilder initial(final float initial) {
    set("initial", String.valueOf(initial));
    return this;
  }

  public SliderBuilder stepSize(final float stepSize) {
    set("stepSize", String.valueOf(stepSize));
    return this;
  }

  public SliderBuilder buttonStepSize(final float buttonStepSize) {
    set("buttonStepSize", String.valueOf(buttonStepSize));
    return this;
  }

  @Nonnull
  private static String getName(final boolean vertical) {
    if (vertical) {
      return "verticalSlider";
    } else {
      return "horizontalSlider";
    }
  }
}
