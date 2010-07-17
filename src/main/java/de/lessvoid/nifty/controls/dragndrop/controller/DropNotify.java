package de.lessvoid.nifty.controls.dragndrop.controller;

public interface DropNotify {
  void dropped(DroppableControl source, DraggableControl draggable, DroppableControl target);
}
