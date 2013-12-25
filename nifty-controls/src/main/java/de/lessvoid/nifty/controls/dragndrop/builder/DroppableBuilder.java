package de.lessvoid.nifty.controls.dragndrop.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;

public class DroppableBuilder extends ControlBuilder {
  public DroppableBuilder() {
    super("droppable");
  }

  public DroppableBuilder(@Nonnull final String id) {
    super(id, "droppable");
  }
}
