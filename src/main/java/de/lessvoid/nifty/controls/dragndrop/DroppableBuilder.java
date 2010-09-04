package de.lessvoid.nifty.controls.dragndrop;

import de.lessvoid.nifty.builder.ControlBuilder;

public class DroppableBuilder extends ControlBuilder {
  public DroppableBuilder(final String name) {
    super("droppable");
  }

  public DroppableBuilder(final String name, final String id) {
    super(id, "droppable");
  }
}
