package de.lessvoid.nifty.controls.slider.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class SliderBuilder extends ControlBuilder {
  public SliderBuilder(final boolean vertical) {
    super(getName(vertical));
  }

  public SliderBuilder(final String id, final boolean vertical) {
    super(id, getName(vertical));
  }

  public void min(final float min) {
    set("min", String.valueOf(min));
  }

  public void max(final float max) {
    set("max", String.valueOf(max));
  }

  public void initial(final float initial) {
    set("initial", String.valueOf(initial));
  }

  public void stepSize(final float stepSize) {
    set("stepSize", String.valueOf(stepSize));
  }

  public void buttonStepSize(final float buttonStepSize) {
    set("buttonStepSize", String.valueOf(buttonStepSize));
  }

  private static String getName(final boolean vertical) {
    if (vertical) {
      return "verticalSlider";
    } else {
      return "horizontalSlider";
    }
  }
}
