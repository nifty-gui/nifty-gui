package de.lessvoid.nifty.controls;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DroppableDropFilter {
  boolean accept(@Nullable Droppable source, @Nonnull Draggable draggable, @Nonnull Droppable target);
}
