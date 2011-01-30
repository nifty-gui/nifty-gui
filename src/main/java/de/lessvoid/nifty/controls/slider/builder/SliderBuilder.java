package de.lessvoid.nifty.controls.slider.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class SliderBuilder extends ControlBuilder {
  public SliderBuilder(final boolean vertical) {
    super(vertical ? "verticalSlider" : "horizontalSlider");
  }

  public SliderBuilder(final String id, final boolean vertical) {
    super(id, vertical ? "verticalSlider" : "horizontalSlider");
  }
}
