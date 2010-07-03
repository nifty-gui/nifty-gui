package de.lessvoid.nifty.examples.dragndrop;

public interface DropFilter {
  boolean accept(DroppableControl source, DraggableControl draggable, DroppableControl target);
}
