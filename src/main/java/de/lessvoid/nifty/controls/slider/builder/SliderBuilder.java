package de.lessvoid.nifty.controls.slider.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class SliderBuilder extends ControlBuilder {
  public SliderBuilder(final boolean vertical) {
    super(getName(vertical));
  }

  public SliderBuilder(final String id, final boolean vertical) {
    super(id, getName(vertical));
  }

  private static String getName(final boolean vertical) {
    if (vertical) {
      return "verticalSlider";
    } else {
      return "horizontalSlider";
    }
  }
}
