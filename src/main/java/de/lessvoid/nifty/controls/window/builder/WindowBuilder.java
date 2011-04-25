package de.lessvoid.nifty.controls.window.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class WindowBuilder extends ControlBuilder {
  public WindowBuilder() {
    super("window");
  }

  public WindowBuilder(final String id, final String title) {
    super(id, "window");
    set("title", title);
  }

  public void closeable(final boolean closeable) {
    set("closable", String.valueOf(closeable));
  }
}
