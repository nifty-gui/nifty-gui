package de.lessvoid.nifty.controls;


import javax.annotation.Nonnull;

public interface DroppableDropFilter {
  boolean accept(@Nonnull Droppable source, @Nonnull Draggable draggable, @Nonnull Droppable target);
}
