package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DroppableDroppedEvent implements NiftyEvent {
  @Nullable
  private final Droppable source;
  @Nonnull
  private final Draggable draggable;
  @Nonnull
  private final Droppable target;

  public DroppableDroppedEvent(
      @Nullable final Droppable source,
      @Nonnull final Draggable draggable,
      @Nonnull final Droppable target) {
    this.source = source;
    this.draggable = draggable;
    this.target = target;
  }

  @Nullable
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
