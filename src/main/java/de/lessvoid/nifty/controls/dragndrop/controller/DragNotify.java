package de.lessvoid.nifty.controls.dragndrop.controller;

public interface DragNotify {
  void dragStarted(DroppableControl source, DraggableControl draggable);

  void dragCanceled(DroppableControl source, DraggableControl draggable);
}
