package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

@SuppressWarnings("rawtypes")
public class DraggableDragCanceledEvent implements NiftyEvent {
  private Droppable source;
  private Draggable draggable;

  public DraggableDragCanceledEvent(final Droppable source, final Draggable draggable) {
    this.source = source;
    this.draggable = draggable;
  }

  public Droppable getSource() {
    return source;
  }

  public Draggable getDraggable() {
    return draggable;
  }
}
