package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

public class DroppableDroppedEvent implements NiftyEvent {
  @Nonnull
  private final Droppable source;
  @Nonnull
  private final Draggable draggable;
  @Nonnull
  private final Droppable target;

  public DroppableDroppedEvent(
      @Nonnull final Droppable source,
      @Nonnull final Draggable draggable,
      @Nonnull final Droppable target) {
    this.source = source;
    this.draggable = draggable;
    this.target = target;
  }

  @Nonnull
  public Droppable getSource() {
    return source;
  }

  @Nonnull
  public Draggable getDraggable() {
    return draggable;
  }

  @Nonnull
  public Droppable getTarget() {
    return target;
  }
}
