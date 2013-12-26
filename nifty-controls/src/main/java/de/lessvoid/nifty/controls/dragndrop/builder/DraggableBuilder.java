package de.lessvoid.nifty.controls.dragndrop.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class DraggableBuilder extends ControlBuilder {
  public DraggableBuilder() {
    super("draggable");
  }

  public DraggableBuilder(@Nonnull final String id) {
    super(id, "draggable");
  }

  public void handle(@Nonnull final String handleId) {
    set("handle", handleId);
  }

  public void revert(final boolean revert) {
    set("revert", String.valueOf(revert));
  }

  public void drop(final boolean drop) {
    set("drop", String.valueOf(drop));
  }
}
