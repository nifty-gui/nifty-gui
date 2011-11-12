package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

@SuppressWarnings("rawtypes")
public class DraggableDragStartedEvent implements NiftyEvent {
  private Droppable source;
  private Draggable draggable;

  public DraggableDragStartedEvent(final Droppable source, final Draggable draggable) {
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
