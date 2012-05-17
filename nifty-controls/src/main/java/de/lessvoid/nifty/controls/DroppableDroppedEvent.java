package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

public class DroppableDroppedEvent implements NiftyEvent {
  private Droppable source;
  private Draggable draggable;
  private Droppable target;

  public DroppableDroppedEvent(final Droppable source, final Draggable draggable, final Droppable target) {
    this.source = source;
    this.draggable = draggable;
    this.target = target;
  }

  public Droppable getSource() {
    return source;
  }

  public Draggable getDraggable() {
    return draggable;
  }

  public Droppable getTarget() {
    return target;
  }  
}
