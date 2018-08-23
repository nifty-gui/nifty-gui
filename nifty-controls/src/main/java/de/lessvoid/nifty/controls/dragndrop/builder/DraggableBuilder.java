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

  public DraggableBuilder handle(@Nonnull final String handleId) {
    set("handle", handleId);
    return this;
  }

  public DraggableBuilder revert(final boolean revert) {
    set("revert", String.valueOf(revert));
    return this;
  }

  public DraggableBuilder drop(final boolean drop) {
    set("drop", String.valueOf(drop));
    return this;
  }
}
