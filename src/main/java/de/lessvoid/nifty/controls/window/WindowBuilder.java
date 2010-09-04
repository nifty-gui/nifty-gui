package de.lessvoid.nifty.controls.window;

import de.lessvoid.nifty.builder.ControlBuilder;

public class WindowBuilder extends ControlBuilder {
  public WindowBuilder(final String name) {
    super("window");
  }

  public WindowBuilder(final String name, final String id) {
    super(id, "window");
  }
}
