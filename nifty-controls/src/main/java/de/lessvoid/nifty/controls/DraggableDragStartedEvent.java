package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

public class DraggableDragStartedEvent implements NiftyEvent {
  @Nonnull
  private final Droppable source;
  @Nonnull
  private final Draggable draggable;

  public DraggableDragStartedEvent(@Nonnull final Droppable source, @Nonnull final Draggable draggable) {
    this.source = source;
    this.draggable = draggable;
  }

  @Nonnull
  public Droppable getSource() {
    return source;
  }

  @Nonnull
  public Draggable getDraggable() {
    return draggable;
  }
}
