package de.lessvoid.nifty.controls.console.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class ConsoleBuilder extends ControlBuilder {
  public ConsoleBuilder(final String id) {
    super(id, "nifty-console");
  }

  public ConsoleBuilder(final String id, final int lines) {
    super(id, "nifty-console");
    setLines(lines);
  }

  public void lines(final int lines) {
    set("lines", String.valueOf(lines));
  }

  private void setLines(final int lines) {
    set("lines", String.valueOf(lines));
  }
}
