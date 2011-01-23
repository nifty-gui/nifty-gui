package de.lessvoid.nifty.controls.slider.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class SliderBuilder extends ControlBuilder {
  public SliderBuilder(final boolean vertical, final String name) {
    super(vertical ? "verticalSlider" : "horizontalSlider");
  }

  public SliderBuilder(final boolean vertical, final String name, final String id) {
    super(id, vertical ? "verticalSlider" : "horizontalSlider");
  }
}
