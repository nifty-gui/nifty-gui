package de.lessvoid.nifty.controls.window.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class WindowBuilder extends ControlBuilder {
  public WindowBuilder() {
    super("window");
  }

  public WindowBuilder(@Nonnull final String id, @Nonnull final String title) {
    super(id, "window");
    set("title", title);
  }

  public void closeable(final boolean closeable) {
    set("closeable", String.valueOf(closeable));
  }

  public void hideOnClose(final boolean hideOnClose) {
    set("hideOnClose", String.valueOf(hideOnClose));
  }
  
  public void minimized(final boolean minimized) {
      set("minimized", String.valueOf(minimized));
  }
}
