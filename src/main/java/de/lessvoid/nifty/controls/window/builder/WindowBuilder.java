package de.lessvoid.nifty.controls.window.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class WindowBuilder extends ControlBuilder {
  public WindowBuilder(final String title) {
    super("window");
    set("title", title);
  }

  public WindowBuilder(final String title, final String id) {
    super(id, "window");
    set("title", title);
  }

  public void closeable(final boolean closeable) {
    set("closable", String.valueOf(closeable));
  }
}
