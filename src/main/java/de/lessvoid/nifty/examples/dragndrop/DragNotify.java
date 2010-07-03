package de.lessvoid.nifty.examples.dragndrop;

public interface DragNotify {
  void dragStarted(DroppableControl source, DraggableControl draggable);

  void dragCanceled(DroppableControl source, DraggableControl draggable);
}
