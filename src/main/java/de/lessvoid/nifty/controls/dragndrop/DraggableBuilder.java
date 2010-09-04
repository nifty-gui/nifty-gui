package de.lessvoid.nifty.controls.dragndrop;

import de.lessvoid.nifty.builder.ControlBuilder;

public class DraggableBuilder extends ControlBuilder {
  public DraggableBuilder(final String name) {
    super("draggable");
  }

  public DraggableBuilder(final String name, final String id) {
    super(id, "draggable");
  }
}
