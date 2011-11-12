package de.lessvoid.nifty.controls;


public interface DroppableDropFilter {
  boolean accept(Droppable source, Draggable draggable, Droppable target);
}
