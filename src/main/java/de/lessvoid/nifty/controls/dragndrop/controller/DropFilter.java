package de.lessvoid.nifty.controls.dragndrop.controller;

public interface DropFilter {
  boolean accept(DroppableControl source, DraggableControl draggable, DroppableControl target);
}
