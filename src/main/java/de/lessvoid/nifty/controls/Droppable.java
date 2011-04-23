package de.lessvoid.nifty.controls;

public interface Droppable extends NiftyControl {

  void addFilter(DroppableDropFilter droppableDropFilter);

  void removeFilter(DroppableDropFilter filter);

  void removeAllFilters();

}
