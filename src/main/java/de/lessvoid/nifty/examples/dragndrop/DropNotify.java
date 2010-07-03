package de.lessvoid.nifty.examples.dragndrop;

public interface DropNotify {
  void dropped(DroppableControl source, DraggableControl draggable, DroppableControl target);
}
